/**
 * Reconstitutes target haplotype with two field only
 * Perform multiallelic TDT at protein level
 * Remove ambiguities => consolidate haplotypes
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import workshop.haplotype.ambiguity.ExtractTwoField;
import workshop.haplotype.organize.NonRedundantList;

/**
 * @author kazu
 * @version March 21 2018
 *
 */
public class SampleTwoFieldHap extends SampleHap {
	protected NonRedundantList twoFieldHaplotypes;	// store unique haplotypes
	protected Map<String, List<String>> sampleTwoFieldHap;	// sample haplotype

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public SampleTwoFieldHap(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		twoFieldHaplotypes = new NonRedundantList();
		sampleTwoFieldHap = new HashMap<String, List<String>>();
		for (String hap : haplotypes.getList()) {					
			twoFieldHaplotypes.addNonRedundantList(convertTwoFieldHaplotype(hap));
		}
		
		for (String sample : sampleHap.keySet()) {
			List<String> list = new ArrayList<String>(); 
			for (String hap : sampleHap.get(sample)) {
				list.add(convertTwoFieldHaplotype(hap));
			}
			sampleTwoFieldHap.put(sample, list);
		}
	}
	
	public String convertTwoFieldHaplotype(String hap) {
		String [] alleles = hap.split("~");
		String tmp = "";
		for (String allele : alleles) {
			ExtractTwoField extracted = new ExtractTwoField(allele);		// extract two field
			int count = 0;
			String ambiguity = "";
			for (String str : extracted.getExtractedTwoFieldType().getList()) {
				if (count > 0) {
					ambiguity += "/";
				}
				else {
					ambiguity += str;
				}
				count++;
			}
			tmp += ambiguity + "~";
		}	
		String haplotype = tmp.substring(0, tmp.length() -1);
		
		return haplotype;
	}
	
	public NonRedundantList getTwoFieldHaplotypes() {
		return twoFieldHaplotypes;
	}
	
	public Map<String, List<String>> getSampleTwoFieldHap() {
		return sampleTwoFieldHap;
	}


}
