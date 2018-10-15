/**
 * This is to generate standard TDT file for R package
 * modified to be able to generate input file 
 * two-filed allele name and
 * full allele name
 */
package workshop.haplotype.frequency.ms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kazu
 * @version April 10 2018
 *
 */
public class SampleHapCountForRpackageTDT extends SampleTwoFieldHap {
	// family
	// sample
	// haplotype
	// count
	protected Map<String, Map<String, Map<String, Integer>>> famSampleTwoFieldHapCount;
	protected Map<String, Map<String, Map<String, Integer>>> famSampleFullHapCount;
	protected final String [] relation = { "father", "mother", "child" };	// fixed order for TDT

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public SampleHapCountForRpackageTDT(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		famSampleTwoFieldHapCount = new HashMap<String, Map<String, Map<String, Integer>>>();
		famSampleFullHapCount = new HashMap<String, Map<String, Map<String, Integer>>>();
		
		for (String family : familyList.getList()) {	// go through family
			Map<String, Map<String, Integer>> twoMap = new HashMap<String, Map<String, Integer>>();
			Map<String, Map<String, Integer>> fullMap = new HashMap<String, Map<String, Integer>>();
			
			for (String re : relation) {	// go through relationship
				if (re.equals("father")) {
					twoMap.putAll(countHap(family, fatherList, 
							twoFieldHaplotypes.getList(), sampleTwoFieldHap));
					fullMap.putAll(countHap(family, fatherList, 
							haplotypes.getList(), sampleHap));
				}
				else if (re.equals("mother")) {
					twoMap.putAll(countHap(family, motherList, 
							twoFieldHaplotypes.getList(), sampleTwoFieldHap));
					fullMap.putAll(countHap(family, motherList, 
							haplotypes.getList(), sampleHap));
				}
				else {
					twoMap.putAll(countHap(family, childList, 
							twoFieldHaplotypes.getList(), sampleTwoFieldHap));
					fullMap.putAll(countHap(family, childList, 
							haplotypes.getList(), sampleHap));
				}
			}
			famSampleTwoFieldHapCount.put(family, twoMap);		
			famSampleFullHapCount.put(family, fullMap);
		}		
	}
	
	public String [] getRelation() {
		return relation;
	}
	
	public Map<String, Map<String, Map<String, Integer>>> getFamSampleTwoFieldHapCount() {
		return famSampleTwoFieldHapCount;
	}
	
	public Map<String, Map<String, Map<String, Integer>>> getFamSampleFullHapCount() {
		return famSampleFullHapCount;
	}
	
	public Map<String, Map<String, Integer>> countHap(String family, List<String> sampleList,
			List<String> hapList, Map<String, List<String>> sampleHapList) {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		for (String sample : sampleList) {
			if (sampleFam.get(sample).equals(family)) {
				// key: haplotype
				// values: count
				Map<String, Integer> tmpMap = new HashMap<String, Integer>();
				for (String hap: hapList) {
					int count = 0;
					if (sampleHapList.get(sample).contains(hap)) {
						for (String type : sampleHapList.get(sample)) {
							if (type.equals(hap)) {
								count++;
							}
						}
					}
					tmpMap.put(hap, count);
				}
				map.put(sample, tmpMap);
				break;					
			}
		}
		return map;
	}

}
