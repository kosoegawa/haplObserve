/**
 * This is to replace Perl and shell scripts
 * This program generate combined table
 * global, ASI, EUR, AFA, HIS etc
 * the table has "ranking" for each haplotype for each population
 * the table is sorted by global ranking in ascending order
 * 
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import workshop.haplotype.frequency.ranking.OrderbyRanking;
import workshop.haplotype.frequency.ranking.SampleHapCount;
import workshop.haplotype.organize.file.CreateDirectoryList;

/**
 * @author kazu
 * @version April 5 2018
 *
 */
public class GenerateGlobalGroupsHapCountTable {

	/**
	 * @param filePath to global directory
	 * @param target loci "HLA-C,HLA-B"
	 * @param output => output file
	 */
	public GenerateGlobalGroupsHapCountTable( String filePath, String target, String output ) {
		// TODO Auto-generated constructor stub
		CreateDirectoryList cdl = new CreateDirectoryList(filePath);	// generate list of dir (group) & inputfiles
		SampleHapCount globalshc = new SampleHapCount(cdl.getGlobalFam(), target);	// global/haplotype/FAM_Haplotype_Summary_GL_String_
		
		try {
			BufferedWriter out = 
				new BufferedWriter(new FileWriter(output));	
			
			String repeat = "";	// add empty wells
			String [] loci = target.split(",");
			for (int index = 0; index < loci.length; index++) {
				repeat += ",";
			}
			
			List<Integer> grParentList = new ArrayList<Integer>();	// populate group parent count
			List<Integer> grSubList = new ArrayList<Integer>();	// populate group subject count
			List<Integer> grParentHapCount = new ArrayList<Integer>();	// populate group parent hap count
			for (String group : cdl.getInputFileNameList()) {	// AFA/FAM_Haplotype_
				SampleHapCount groupshc = new SampleHapCount(group, target);									
				int grParent = groupshc.getSampleHap().getFatherList().size() + groupshc.getSampleHap().getMotherList().size();
				grParentList.add(grParent);
				grSubList.add(groupshc.getSampleHap().getSampleList().size());
				grParentHapCount.add(groupshc.getParentHapCount());							
			}
			// Global parent
			int parent = globalshc.getSampleHap().getFatherList().size() + globalshc.getSampleHap().getMotherList().size();
			out.write("Parent Count" + repeat + parent + ",,,,,");		
			for (Integer grParent : grParentList) {	// go through group parent count
				out.write(grParent + ",,,,,");	
			}
			out.write("\n");	// line1
			
			out.write("Subject Count" + repeat + globalshc.getSampleHap().getSampleList().size() + ",,,,,");
			for (Integer subjectCount : grSubList) {	// go through group subject count
				out.write(subjectCount + ",,,,,");
			}
			out.write("\n");	// line2
			
			out.write("Parent HapCount" + repeat + globalshc.getParentHapCount() + ",,,,,");
			for (Integer hapCount : grParentHapCount) {	// go through group parent haplotype count
				out.write(hapCount + ",,,,,");
			}
			out.write("\n");	// line 3
			
			out.write(repeat + "Global,Global,Global,Global,Global,");
			for ( String group : cdl.getDirList() ) {	// go through group ID => ethnic group
				out.write(group + "," + group + "," + group + "," + group + "," + group + ",");
			}
			out.write("\n" + repeat);	// added repeat here for the next line to save line of code
			
			String [] strList = {"Hap", "Hap", "Hap", "Family", "Sample"};
			for (int index = 0; index < cdl.getDirList().size() + 1; index++) {	// +1 is for global
				for (String str : strList) {
					out.write(str + ",");
				}			
			}
			out.write("\n");	// line 5
			
			for (String locus : loci) {	// write loci
				if (!locus.contains("HLA-")) { out.write("HLA-" + locus + ","); }
				else { out.write(locus + ","); }
			}			
			String [] countList = {"Count","Frequency","Rank","Count","Count"};
			for (int index = 0; index < cdl.getDirList().size() + 1; index++) {	// +1 is for global
				for (String str : countList) {
					out.write(str + ",");
				}			
			}
			out.write("\n");	// line 6
			
			OrderbyRanking obr = new OrderbyRanking(globalshc);	// generate ranked haplotype list
			for (String hap : obr.getRankedHapList()) {		// use ordered hap list
				String [] alleles = hap.split("~");
				for (String allele : alleles) {	// need to handle DRB345					
					if (target.contains("DRB[345]")) {		// target contains DRB345
						if ((hap.contains("DRB3")) || (hap.contains("DRB4")) ||	(hap.contains("DRB5"))) {	
							out.write(allele + ",");	// hap contains DRB345				
						}
						else {	// hap does not contain DRB345
							if (allele.contains("HLA-DRB1")) {
								out.write("," + allele + ",");	// add one column for empty DRB345 before DRB1
							}
							else {
								out.write(allele + ",");	// other allele
							}							
						}									
					}
					else {	// no DRB345 in the target
						out.write(allele + ",");
					}				
				}
				// values for global
				if (globalshc.getHapCountRank().containsKey(hap)) {
					out.write(globalshc.getHapCountRank().get(hap).get(0) + ",");	// hap count
					out.write(String.format("%.6f", globalshc.getFrequency().get(hap)) + ",");	// frequency
					for (int index = 1; index < globalshc.getHapCountRank().get(hap).size(); index++) {	// ranking, etc
						out.write(globalshc.getHapCountRank().get(hap).get(index) + ",");	// 
					}
				}
				else {	// haplotype does not exist
					for (int index = 0; index < 5; index++) {
						if (index != 2) {
							out.write("0,");	// add 0 except for ranking
						}
						else {	// add ranking for 0 => bottom of the ranking
							out.write(globalshc.getNextLastRank() + ",");	
						}						
					}					
				}
				
				for (String group : cdl.getInputFileNameList()) {	// go though input file name
					SampleHapCount groupshc = new SampleHapCount( group, target );
					if (groupshc.getHapCountRank().containsKey(hap)) {
						out.write(groupshc.getHapCountRank().get(hap).get(0) + ",");	// hap count
						out.write(String.format("%.6f", groupshc.getFrequency().get(hap)) + ",");	// frequency
						for (int index = 1; index < groupshc.getHapCountRank().get(hap).size(); index++) {
							out.write( groupshc.getHapCountRank().get(hap).get(index) + ",");	// 
						}
					}
					else {
						for (int index = 0; index < 5; index++) {
							if (index != 2) {
								out.write("0,");	
							}
							else {
								out.write(groupshc.getNextLastRank() + ",");
							}						
						}	
					}
				}
				out.write("\n");				
			}		
			
			out.close();					
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot write in " + output);
		}	
	}

}
