/**
 * define category
 * separate haplotypes by category
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import workshop.haplotype.organize.NonRedundantList;

/**
 * @author kazu
 * @version February 13 2018
 *
 */
public class HapCount extends FictitiousChildHap {	//SampleHap
	protected final String []  category = {"father", "dadTrans", "dadNonT", "dadHomo",
			"mother", "momTrans", "momNonT", "momHomo",
			"child", "childNonHomo", "childHomo",
			"fictitious", "ficNonHomo", "ficHomo"};
	// haplotype
	// category
	// sample
	protected Map<String, Map<String, List<String>>> hapCategorySampleList;
	// category
	// count
	protected Map<String, Integer> totalHapCount;	// used to calculate frequency

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public HapCount(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		totalHapCount = new HashMap<String, Integer>();
		hapCategorySampleList = new HashMap<String, Map<String, List<String>>>();
		
		int dadCount = 0;
		int momCount = 0;
		int childCount = 0;
		for (String haplotype : haplotypes.getList()) {		// go through haplotype
			
			List<String> dad = new ArrayList<String>();
			NonRedundantList dadTrans = new NonRedundantList();
			NonRedundantList dadNonT = new NonRedundantList();
			NonRedundantList dadHomo = new NonRedundantList();
			
			List<String> mom = new ArrayList<String>();
			NonRedundantList momTrans = new NonRedundantList();
			NonRedundantList momNonT = new NonRedundantList();
			NonRedundantList momHomo = new NonRedundantList();
			
			List<String> kid = new ArrayList<String>();
			NonRedundantList kidNonHomo = new NonRedundantList();
			NonRedundantList kidHomo = new NonRedundantList();
			
			List<String> fictitious = new ArrayList<String>();
			NonRedundantList ficNonHomo = new NonRedundantList();
			NonRedundantList ficHomo = new NonRedundantList();
			for (String sample : sampleList) {	//go through sample			
				if (sampleHap.get(sample).contains(haplotype)) {

					if (sampleRelation.get(sample).contains("father")) {		// father
						if (sampleHap.get(sample).get(0).equals(haplotype)) {	// transmitted haplotype
							dad.add(sample);	// populate dad
							dadTrans.addNonRedundantList(sample);	// populate transmitted
							dadCount++;
						}
						if (sampleHap.get(sample).get(1).equals(haplotype)) {	// non-transmitted haplotype
							dad.add(sample);	// populate dad
							dadNonT.addNonRedundantList(sample);
							dadCount++;
						}
						
						if ((sampleHap.get(sample).get(0).equals(haplotype)) && 
						(sampleHap.get(sample).get(1).equals(haplotype))) {	// homozygous
							dadHomo.addNonRedundantList(sample);
						}						
					}
					else if (sampleRelation.get(sample).contains("mother")) {	// mother
						if (sampleHap.get(sample).get(0).equals(haplotype)) {	// transmitted
							mom.add(sample);	// populate mom
							momTrans.addNonRedundantList(sample);
							momCount++;
						}
						if (sampleHap.get(sample).get(1).equals(haplotype)) {
							mom.add(sample);	// populate mom
							momNonT.addNonRedundantList(sample);	// non-transmitted
							momCount++;
						}
						
						if ((sampleHap.get(sample).get(0).equals(haplotype)) && 
						(sampleHap.get(sample).get(1).equals(haplotype))) {
							momHomo.addNonRedundantList(sample);	// homozygous
						}
						
					}
					else if (sampleRelation.get(sample).contains("child")) {	// child						
						if ((sampleHap.get(sample).get(0).equals(haplotype)) && 
								(sampleHap.get(sample).get(1).equals(haplotype))) {
									kidHomo.addNonRedundantList(sample);	// homozygous
									kid.add(sample);
									kid.add(sample);	// duplicate for homozygous
									childCount++;
									childCount++;
						}
						else {		// heterozygous
							kid.add(sample);
							kidNonHomo.addNonRedundantList(sample);	// populate child
							childCount++;
						}					
					}	
					else{	// fictitious
						if ((sampleHap.get(sample).get(0).equals(haplotype)) && 
								(sampleHap.get(sample).get(1).equals(haplotype))) {
							ficHomo.addNonRedundantList(sample);
							fictitious.add(sample);
							fictitious.add(sample);
						}
						else {
							ficNonHomo.addNonRedundantList(sample);
							fictitious.add(sample);
						}
						
					}
				}								
			}
			Map<String, List<String>> tmpMap = new HashMap<String, List<String>>();
			tmpMap.put(category[0], dad);
			tmpMap.put(category[1], dadTrans.getList());
			tmpMap.put(category[2], dadNonT.getList());
			tmpMap.put(category[3], dadHomo.getList());
			tmpMap.put(category[4], mom);
			tmpMap.put(category[5], momTrans.getList());
			tmpMap.put(category[6], momNonT.getList());
			tmpMap.put(category[7], momHomo.getList());
			tmpMap.put(category[8], kid);
			tmpMap.put(category[9], kidNonHomo.getList());
			tmpMap.put(category[10], kidHomo.getList());
			tmpMap.put(category[11], fictitious);
			tmpMap.put(category[12], ficNonHomo.getList());
			tmpMap.put(category[13], ficHomo.getList());
			hapCategorySampleList.put(haplotype, tmpMap);
		}
		totalHapCount.put(category[0], dadCount);
		totalHapCount.put(category[4], momCount);
		totalHapCount.put(category[8], childCount);
	}
	
	public Map<String, Map<String, List<String>>> getHapCategorySampleList() {
		return hapCategorySampleList;
	}
	
	public String [] getCategory() {
		return category;
	}
	
	public Map<String, Integer> getTotalHapCount() {
		return totalHapCount;
	}

}
