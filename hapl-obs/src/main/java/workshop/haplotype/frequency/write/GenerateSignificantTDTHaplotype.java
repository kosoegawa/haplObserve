/**
 * Extracts haplotypes and alleles that has Chi Square values > 4.0
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import workshop.haplotype.frequency.ms.HapTDT;
import workshop.haplotype.frequency.ranking.HapTarget;
import workshop.haplotype.gene.OrderedHLAgene;

/**
 * @author kazu
 * @version October 12 2018
 *
 */
public class GenerateSignificantTDTHaplotype extends GenerateHaplotypeTDT {

	/**
	 * @param global
	 */
	public GenerateSignificantTDTHaplotype(String global) {
		super(global);
		// TODO Auto-generated constructor stub
		HapTarget ht = new HapTarget();		// LOOK INTO HERE
		OrderedHLAgene orderedHlaGene = new OrderedHLAgene();
		String output = global + "summary/Significant_TDT_Haplotypes" + today + ".csv";
		System.out.println("Processing: Significant_TDT_Haplotypes");
		
		try {
			BufferedWriter out = 
					new BufferedWriter(new FileWriter(output));	
			
			for (int index = 0; index < orderedHlaGene.getOrderedGeneList().size(); index++) {
				out.write(",");
			}
			for (int index = 0; index < 5; index ++) {
				out.write("Father,");
			}
			for (int index = 0; index < 5; index ++) {
				out.write("Mother,");
			}
			for (int index = 0; index < 4; index ++) {	// 2 => 4
				out.write("Child,");
			}
			for (int index = 0; index < 3; index ++) {	// 0 => 4
				out.write("FictitiousChild,");
			}
			out.write("Parent,");
			out.write("Family,");
			out.write("Sample,");
			out.write("BothParentHomo\n");
			for (int index = 0; index < orderedHlaGene.getOrderedGeneList().size(); index++) {
				out.write(",");
			}
			String [] second = 
				{"Hap","Frequency","Transmitted","Non-Transmitted","Homo","Hap,Frequency","Transmitted",	
					"Non-Transmitted","Homo","Hap","NonHomo","Homo","Frequency","Hap","NonHomo","Homo",
					"Frequency","Hap","Hap","Hap"};
			for (String element : second) {
				out.write(element + ",");
			}
			out.write("\n");
			
			for (String gene: orderedHlaGene.getOrderedGeneList()) {
				out.write(gene + ",");
			}
			String [] third = 
				{"Count","%","Count","Count","Count","Count","%","Count","Count","Count",
					"Count", "Count","Count","%", "Count", "Count","Count","%","Count",
					"Count","Count","Chi-Suqare","p-value", "OddsRation", "Lower95%CI", "Upper95%CI"};	
			for (String element : third) {
				out.write(element + ",");
			}
			out.write("\n");
			
			Map<String, Double> hapChiSignificant = new HashMap<String, Double>();
			Set<Double> doubleSet = new TreeSet<Double>();
			Set<HapTDT> hapTDTSet = new HashSet<HapTDT>();
			
			for (String target : ht.getHapTargetList()) {	
				String [] fields = {"FAM", "TwoFieldAllele"};
				for (String field : fields) {
					HapTDT tdt = new HapTDT(haplotype + field +"_Haplotype_Summary_GL_String_" + today + ".csv", 
							target);
					
					for (String hap : tdt.getHaplotypes().getList()) {								
						if (hap.equals("ABS")) {
							continue;
						}	
						if (tdt.getHapChi().containsKey(hap)) {
							if ((tdt.getHapChi().get(hap)) < 4.0) {
								continue;
							}
						}
						else {
							continue;
						}
						hapTDTSet.add(tdt);
						hapChiSignificant.put(hap, tdt.getHapChi().get(hap));
						doubleSet.add(tdt.getHapChi().get(hap));					
					}	
				}							
			}
			
			List<String> orderedHap = new ArrayList<String>();
			for (Double value : doubleSet) {
				for (String hap : hapChiSignificant.keySet()) {	
					if (value.equals(hapChiSignificant.get(hap))) {
						orderedHap.add(hap);
					}
				}
			}
			
			for (int index = orderedHap.size() -1; index > 0; index--) {
				for (HapTDT hapTDT : hapTDTSet) {
					for (String hap : hapTDT.getHapChi().keySet()) {
						if (hap.equals(orderedHap.get(index))) {
//							System.out.println(hap);
							String [] alleles = hap.split("~");
							for (String gene: orderedHlaGene.getOrderedGeneList()) {	// go through gene
								int found = 0;
								for (String allele : alleles) {		// go through allele
									if (!gene.equals("HLA-DRB345")) {	// not DRB345						
										if (allele.contains(gene)) {
											out.write(allele + ",");
											found++;
										}
									}
									else {		// gene => DRB345	
										if ((allele.contains("DRB3")) || (allele.contains("DRB4")) ||
												(allele.contains("DRB5"))) {
//											if (!allele.contains("00:00")) {
												out.write(allele + ",");			
												found++;
//											}											
										}													
									}						
								}
								if (found == 0) {
									out.write(",");
								}
							}
							
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("father").size() + ",");					
							out.write(String.format("%.2f", hapTDT.getHapFrequency().get(hap).get("father")) + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("dadTrans").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("dadNonT").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("dadHomo").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("mother").size() + ",");
							out.write(String.format("%.2f", hapTDT.getHapFrequency().get(hap).get("mother")) + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("momTrans").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("momNonT").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("momHomo").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("child").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("childNonHomo").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("childHomo").size() + ",");
							out.write(String.format("%.2f", hapTDT.getHapFrequency().get(hap).get("child")) + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("fictitious").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("ficNonHomo").size() + ",");
							out.write(hapTDT.getHapCategorySampleList().get(hap).get("ficHomo").size() + ",");
							out.write(String.format("%.2f", hapTDT.getHapFrequency().get(hap).get("parent")) + ",");
							out.write(hapTDT.getHapFamilyList().get(hap).size() + ",");
							out.write(hapTDT.getHapSubjectCount().get(hap) + ",");
							out.write(hapTDT.getHapHomoFamilyList().get(hap).size() + ",");
							if (hapTDT.getHapChi().containsKey(hap)) {
								out.write(String.format("%.2f",hapTDT.getHapChi().get(hap)) + ",");
							}
							else {
								out.write(",");
							}
							out.write(hapTDT.getHapPvalue().get(hap) + ",");	
							for (Double odds : hapTDT.getHapOdds().get(hap)) {					
								out.write(String.format("%.2f", odds) + ",");
							}
							out.write("\n");
						}								
					}							
				}	
			}			
			out.close();
		}					
		catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot write in " + output);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GenerateSignificantTDTHaplotype("/Users/kazu/Desktop/ussta1_MSTRIO/");

	}

}
