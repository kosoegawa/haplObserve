/**
 * calculate Chi square value for TDT
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

/**
 * @author kazu
 * @version February 13 2018
 *
 */
public class HapTDT extends HapFrequency {
	// haplotype
	// Chi square value
	protected Map<String, Double> hapChi;
	protected Map<String, Double> hapPvalue;
	// haplotype
	// odds ratio
	// confidence interval max
	// confidence interval min
	protected Map<String, List<Double>> hapOdds;
	protected List<String> orderedHapByChi;

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public HapTDT(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		hapChi = new HashMap<String, Double>();
		hapPvalue = new HashMap<String, Double>();
		
		hapOdds = new HashMap<String, List<Double>>();
		orderedHapByChi = new ArrayList<String>();
		
		Set<Double> doubleSet = new TreeSet<Double>();
		for (String haplotype : haplotypes.getList()) {
			List<Double> list = new ArrayList<Double>();
			double dadTrans = hapCategorySampleList.get(haplotype).get(category[1]).size();
			double dadNonT = hapCategorySampleList.get(haplotype).get(category[2]).size();
			double dadHomo = hapCategorySampleList.get(haplotype).get(category[3]).size();
			double momTrans = hapCategorySampleList.get(haplotype).get(category[5]).size();
			double momNonT = hapCategorySampleList.get(haplotype).get(category[6]).size();
			double momHomo = hapCategorySampleList.get(haplotype).get(category[7]).size();

			double trans = (dadTrans - dadHomo) + (momTrans - momHomo);
//			double trans = dadTrans + momTrans;
			double nonT = (dadNonT - dadHomo) + (momNonT - momHomo); 
//			double nonT = dadNonT + momNonT; 
			if ( (trans > 4) && (nonT > 4) ) {
				// this is from the original paper, but ct.chiSquare will give the same answer
				double chi = (trans - nonT)*(trans - nonT)/(trans + nonT);
				hapChi.put(haplotype, chi);
				doubleSet.add(chi);
				
				ChiSquareTest ct = new ChiSquareTest();
				double [] expected = {(trans+nonT)/2,(trans+nonT)/2};
				long [] observed = {new Double(trans).longValue(), new Double(nonT).longValue()};

				double pValue = ct.chiSquareTest(expected, observed);
				hapPvalue.put(haplotype, pValue);
			}
			else {
				hapChi.put(haplotype, 0.0);	
				hapPvalue.put(haplotype, 1.0);
				doubleSet.add(0.0);
			}
			
/*			if ((trans + nonT) != 0) {
				double chi = (trans - nonT)*(trans - nonT)/(trans + nonT);
				hapChi.put(haplotype, chi);	
			}	*/
			// number of affected children who has this haplotype
			double a = 0.0;	// MS children haplotype
			if (hapCategorySampleList.get(haplotype).get(category[9]).size()	//childNonHomo
					+ hapCategorySampleList.get(haplotype).get(category[10]).size() != 0) {	//childHomo
				a = hapCategorySampleList.get(haplotype).get(category[9]).size()
						+ hapCategorySampleList.get(haplotype).get(category[10]).size();
			}
			// number of remaining children who do not have this haplotype ((total hap)/2 - a)
			double b = 0.05;
			if ((totalHapCount.get(category[8])/2 - a) != 0) {
				b = totalHapCount.get(category[8])/2 - a;
			}
			// number of fictitious "unaffected children who have this haplotype
			double c = 0.05;
			if ((hapCategorySampleList.get(haplotype).get(category[12]).size()		//ficNonHomo
					+ hapCategorySampleList.get(haplotype).get(category[13]).size()) != 0) {	//ficHomo
				c = hapCategorySampleList.get(haplotype).get(category[12]).size()
						+ hapCategorySampleList.get(haplotype).get(category[13]).size();
			}
			// number of fictitious unaffected children who do not have this haplotype ((total hap)/2 - c)
			double d = 0.0;
			if (totalHapCount.get(category[8])/2 - c > 0) {
				d = totalHapCount.get(category[8])/2 - c;
			}
					
			double odds = (a*d)/(b*c);
			list.add(odds);
			double min = Math.exp((Math.log(odds) - 1.96*Math.sqrt(1/a+1/b+1/c+1/d)));
			list.add(min);
			double max = Math.exp((Math.log(odds) + 1.96*Math.sqrt(1/a+1/b+1/c+1/d)));
			list.add(max);
			
			hapOdds.put(haplotype, list);						
		}
		
		for (Double chi : doubleSet) {
			for (String haplotype : hapChi.keySet()) {
				if (chi.equals(hapChi.get(haplotype))) {
					orderedHapByChi.add(haplotype);
				}
			}
		}
	}
	
	public Map<String, Double> getHapChi() {
		return hapChi;
	}
	
	public Map<String, Double> getHapPvalue() {
		return hapPvalue;
	}
	
	public Map<String, List<Double>> getHapOdds() {
		return hapOdds;		
	}
	
	public List<String> getOrderedHapByChi() {
		return orderedHapByChi;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = "/Users/kazu/Desktop/ussta1_MSTRIO/haplotype/FAM_Haplotype_Summary_GL_String_2018-10-10.csv";
//		String target = "HLA-B,HLA-C";
		String target = "DRB[345],HLA-DRB1,HLA-DQB1";
		
		HapTDT tdt = new HapTDT(filePath, target);
		System.out.println();
		for (int index = tdt.getOrderedHapByChi().size() -1; index > 0; index--) {
			String haplotype = tdt.getOrderedHapByChi().get(index);
			System.out.println(haplotype);
			System.out.println(tdt.getHapChi().get(haplotype));		
		}		
	}

}
