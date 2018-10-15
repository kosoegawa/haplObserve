/**
 * Reconstitutes target haplotype
 * capture families that contain a haplotype
 * Note: haplotypes contains unique haplotypes including recombinant haplotypes
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import workshop.haplotype.organize.NonRedundantList;
import workshop.haplotype.organize.file.removeLine.RemoveFirstLine;
import workshop.haplotype.organize.file.removeLine.RemoveLine;
import workshop.haplotype.utilities.FileUtilities;

/**
 * @author kazu
 * @version June 25 2018
 *
 */
public class SampleHap extends SampleHapAbstract {
	// haplotype
	// families that contain a haplotype
	protected Map<String, List<String>> hapFamilyList;	// 
	

	/**
	 * @param filePath: no header
	 * @param targetLoci
	 */
	public SampleHap(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		hapFamilyList = new HashMap<String, List<String>>();
		
		RemoveLine rl = new RemoveFirstLine(FileUtilities.readFile(filePath));	// see GenerateHaplotypeGLStringSummary
		for (String line : rl.getRevisedList()) {		
//		ReadFileOrganizeInList rf = new ReadFileOrganizeInList(filePath);	// read file		
//		for (String line : rf.getOriginalList()) {		// go through line by line

			String replacedLine = line.replaceAll("NoMatch", "NT");		// this was VERY important line
			String [] elements = replacedLine.split(",");
			String family = elements[0];	// first element: family
			familyList.addNonRedundantList(family);		// populate familyList
			String sample = elements[1];	// second element: sample
					
			String regex = "HLA-DRB[345]\\*00:00~";	// remove for two field allele
			// swapped from the original design
			String [] hap = elements[3].replaceAll(regex, "").split("\\+");			// Two haplotypes
			String relation = elements[2];	// third element: relationship
			sampleList.add(sample);
			if ( relation.equals("father") ) {	// fathers
				fatherList.add(sample);
			}
			else if (relation.equals("mother")) {	// mothers
				motherList.add(sample);
			}
			else {	// children
				childList.add(sample);
			}			
			sampleFam.put(sample, family);	// populate sample family
			sampleRelation.put(sample, relation);
			
			List<String> hapList = new ArrayList<String>();
			for (String haplo : hap) {	// go through each haplotype
				
				
				String [] alleles = haplo.split("~");	// separate a haplotype into alleles
				String targetHap = "";	// Reconstitutes target haplotype
				for (String allele : alleles) {		// go through allele 
					for (String locus : loci) {		// go through target loci
//						System.out.println(locus);
						if (allele.contains(locus)) {	//allele contains locus
							targetHap += allele;
							targetHap += "~";
						}
					}
				}
				if (targetHap.length() > 1) {
					targetHap = targetHap.substring(0, targetHap.length() - 1);	// remove ~ at the end
				}
				
				if (!targetHap.contains("NT")) {	// remove haplotype containing NT
					// comment out
					if (targetHap.length() == 0) {		// not sure this is required
						hapList.add("ABS");		
						haplotypes.addNonRedundantList("ABS");	// populate unique haplotype
					}	
					if (targetHap.length() > 0) {
						hapList.add(targetHap);
						haplotypes.addNonRedundantList(targetHap);	// populate unique haplotype
					}					
				}
			}	// end of for loop for hap
			sampleHap.put(sample, hapList);														
		}
		
		// create list to determine how many families have this haplotype
		for (String hap : haplotypes.getList()) {	
			NonRedundantList familyList = new NonRedundantList();
			for (String sample : sampleList) {		// go through sample
				if (sampleHap.get(sample).contains(hap)) {	// list contains hap
					String family = sampleFam.get(sample);
					familyList.addNonRedundantList(family);
//					System.out.println(family);
				}				
			}
			hapFamilyList.put(hap, familyList.getList());
		}
	}
	
	public Map<String, List<String>> getHapFamilyList() {
		return hapFamilyList;
	}	

}
