/**
 * To generate matrix with two-field allele name
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kazu
 * @version March 23 2018
 *
 */
public class SampleTwoFieldHapMatrix extends SampleHapMatrix {
	// populate parental haplotype/allele combination
	// transmitted, non-transmitted
	protected List<List<String>> listOfTwoFieldHapCombination;
	// key: parental haplotype liest
	// values: count
	protected Map<List<String>, Integer> twoFieldHapCombCount;
	// key: parental haplotype liest
	// values: list of indexes of each haplotype
	protected Map<List<String>, List<Integer>> twoFieldHapCombIndex;

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public SampleTwoFieldHapMatrix(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		listOfTwoFieldHapCombination = new ArrayList<List<String>>();	// required
		twoFieldHapCombCount = new HashMap<List<String>, Integer>();	// required
		twoFieldHapCombIndex = new HashMap<List<String>, List<Integer>>();	// required
		
		addTwoFieldValues(fatherList);
		addTwoFieldValues(motherList);
	}
	
	public void addTwoFieldValues(List<String> sampleList) {	// override
		for (String sample : sampleList) {		// go through father list
			if (!listOfTwoFieldHapCombination.contains(sampleTwoFieldHap.get(sample))) { // first incidence				
				listOfTwoFieldHapCombination.add(sampleTwoFieldHap.get(sample));	// add list
				twoFieldHapCombCount.put(sampleTwoFieldHap.get(sample), 1);	// combination of first incidence = 1
				
				// identify position in the haplotype list
				List<Integer> intList = new ArrayList<Integer>();	// tmp list
				for (String hap : sampleTwoFieldHap.get(sample)) {	// go through haplotype list
					intList.add(twoFieldHaplotypes.getList().indexOf(hap));			// capture index in the haplotypes		
				}
				twoFieldHapCombIndex.put(sampleTwoFieldHap.get(sample), intList);	// assign each position to each haplotype
			}
			else {
				int count = twoFieldHapCombCount.get(sampleTwoFieldHap.get(sample)) + 1;
				twoFieldHapCombCount.put(sampleTwoFieldHap.get(sample), count);
			}			
		}
	}
	
	public List<List<String>> getListOfTwoFieldHapCombination() {
		return listOfTwoFieldHapCombination;
	}
	
	public Map<List<String>, Integer> getTwoFieldHapCombCount() {
		return twoFieldHapCombCount;
	}
	
	public Map<List<String>, List<Integer>> getTwoFieldHapCombIndex() {
		return twoFieldHapCombIndex;
	}

}
