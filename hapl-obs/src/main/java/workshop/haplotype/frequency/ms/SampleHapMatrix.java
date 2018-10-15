/**
 * This program is written to organize data for input file for 
 * mtdt2 R package
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kazu
 * @version March 21 2018
 *
 */
public class SampleHapMatrix extends SampleTwoFieldHap {
	// populate parental haplotype/allele combination
	// transmitted, non-transmitted
	protected List<List<String>> listOfHapCombination;
	// key: parental haplotype liest
	// values: count
	protected Map<List<String>, Integer> hapCombCount;
	// key: parental haplotype liest
	// values: list of indexes of each haplotype
	protected Map<List<String>, List<Integer>> hapCombIndex;

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public SampleHapMatrix(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		listOfHapCombination = new ArrayList<List<String>>();	// required
		hapCombCount = new HashMap<List<String>, Integer>();	// required
		hapCombIndex = new HashMap<List<String>, List<Integer>>();	// required
		
		addValues(fatherList);
		addValues(motherList);
	}
	
	public void addValues(List<String> sampleList) {	// override
		for (String sample : sampleList) {		// go through father list
			if (!listOfHapCombination.contains(sampleHap.get(sample))) { // first incidence				
				listOfHapCombination.add(sampleHap.get(sample));	// add list
				hapCombCount.put(sampleHap.get(sample), 1);	// combination of first incidence = 1
				
				// identify position in the haplotype list
				List<Integer> intList = new ArrayList<Integer>();	// tmp list
				for (String hap : sampleHap.get(sample)) {	// go through haplotype list
					intList.add(haplotypes.getList().indexOf(hap));			// capture index in the haplotypes		
				}
				hapCombIndex.put(sampleHap.get(sample), intList);	// assign each position to each haplotype
			}
			else {
				int count = hapCombCount.get(sampleHap.get(sample)) + 1;
				hapCombCount.put(sampleHap.get(sample), count);
			}			
		}
	}
	
	public List<List<String>> getListOfHapCombination() {
		return listOfHapCombination;
	}
	
	public Map<List<String>, Integer> getHapCombCount() {
		return hapCombCount;
	}
	
	public Map<List<String>, List<Integer>> getHapCombIndex() {
		return hapCombIndex;
	}

}
