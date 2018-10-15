/**
 * estimate haplotype frequency
 */
package workshop.haplotype.frequency.ms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import workshop.haplotype.organize.NonRedundantList;

/**
 * @author kazu
 * @version February 13 2018
 *
 */
public class HapFrequency extends HapCount {
	// hap
	// category
	// frequency
	protected Map<String, Map<String, Double>> hapFrequency;
	// number of subjects containing a specific haplotype
	protected Map<String, Integer> hapSubjectCount;
	// hap
	// family List
	protected Map<String, List<String>> hapHomoFamilyList;

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public HapFrequency(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		hapFrequency = new HashMap<String, Map<String, Double>>();
		hapSubjectCount = new HashMap<String, Integer>();	
		hapHomoFamilyList = new HashMap<String, List<String>>();
		
		for (String haplotype : haplotypes.getList()) {
//			System.out.println(haplotype);
			int dadCount = hapCategorySampleList.get(haplotype).get(category[0]).size();
			int momCount = hapCategorySampleList.get(haplotype).get(category[4]).size();
			int childCount = hapCategorySampleList.get(haplotype).get(category[8]).size();
			int parentCount = dadCount + momCount;
			int total = dadCount + momCount + childCount;
			hapSubjectCount.put(haplotype, total);
			
			Map<String, Double> categoryFrequency = new HashMap<String, Double>();
			double dad = dadCount;		// has to cast first
			double dadFrequency = dad/totalHapCount.get(category[0]);
			categoryFrequency.put("father", dadFrequency);
			double mom = momCount;
			double momFrequency = mom/totalHapCount.get(category[4]);
			categoryFrequency.put("mother", momFrequency);
			double child = childCount;
			double kidFrequency = child/totalHapCount.get(category[8]);
			categoryFrequency.put("child", kidFrequency);
			double parent = parentCount;
			double parentFrequency = parent/(totalHapCount.get(category[0]) + totalHapCount.get(category[4]));
			categoryFrequency.put("parent", parentFrequency);
			hapFrequency.put(haplotype, categoryFrequency);
			
			// go through father homo list			
			NonRedundantList familyHomoNonList = new NonRedundantList();
			for (String father : hapCategorySampleList.get(haplotype).get(category[3])) {
				String family = sampleFam.get(father);
				
				// go through mother homo list
				for (String mother : hapCategorySampleList.get(haplotype).get(category[7])) {
					if (sampleFam.get(mother).equals(family)) {
						familyHomoNonList.addNonRedundantList(family);
					}
				}	
			}
			hapHomoFamilyList.put(haplotype, familyHomoNonList.getList());
		}
				
	}
	
	public Map<String, Map<String, Double>> getHapFrequency() {
		return hapFrequency;
	}
	
	public Map<String, Integer> getHapSubjectCount() {
		return hapSubjectCount;
	}
	
	public Map<String, List<String>> getHapHomoFamilyList() {
		return hapHomoFamilyList;
	}
	

}
