/**
 * Organize output summary table in TSV format with the following columns:
 * Locus (HLA-A, HLA-B, ...)
 * Allelotype (HLA-A*02:01:01:01 , ...)
 * Population (Global, AUSTRIA, CHINA, ITALY, ...)
 * Frequency
 * Hap_Count
 * Family_Count
 * Sample_Count
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import workshop.haplotype.frequency.ranking.OrderbyRanking;
import workshop.haplotype.frequency.ranking.SampleHapCount;
import workshop.haplotype.organize.file.CreateDirectoryList;

/**
 * @author kazu
 * @version April 5 2018
 *
 */
public class GenerateGlobalHapTSVTable {

	/**
	 * 
	 */
	public GenerateGlobalHapTSVTable( String filePath, String target, String output ) {
		// TODO Auto-generated constructor stub
		CreateDirectoryList cdl = new CreateDirectoryList(filePath);	// generate list of dir (group) & inputfiles
		SampleHapCount globalshc = new SampleHapCount(cdl.getGlobalFam(), target);	// global/haplotype/FAM_Haplotype_Summary_GL_String_
		try {
			BufferedWriter out = 
				new BufferedWriter(new FileWriter(output));	
			
			out.write("Allele\tEthnicity/Country\tFreq\tAlleleCount\tFamCount\tSampleCount\n");
			
			OrderbyRanking obr = new OrderbyRanking(globalshc);	// generate ranked haplotype list
			for (String hap : obr.getRankedHapList()) {		// use ordered hap list
				if (hap.contains("~")) {
					continue;
				}
				else {
					out.write(hap + "\t");
				}
				
				
/*				String [] alleles = hap.split("~");
				for (String allele : alleles) {	// need to handle DRB345					
					if (target.contains("DRB[345]")) {		// target contains DRB345
						if ((hap.contains("DRB3")) || (hap.contains("DRB4")) ||	(hap.contains("DRB5"))) {	
							out.write(allele + "\t");	// hap contains DRB345				
						}
						else {	// hap does not contain DRB345
							if (allele.contains("HLA-DRB1")) {
								out.write("\t" + allele + "t");	// add one column for empty DRB345 before DRB1
							}
							else {
								out.write(allele + "t");	// other allele
							}							
						}									
					}
					else {	// no DRB345 in the target
						out.write(allele + "\t");
					}				
				}		*/						
				out.write("Global\t");
				
				// values for global
				if (globalshc.getHapCountRank().containsKey(hap)) {
					out.write(String.format("%.6f", globalshc.getFrequency().get(hap)) + "\t");	// frequency
					out.write(globalshc.getHapCountRank().get(hap).get(0) + "\t");	// hap count
					
					for (int index = 2; index < globalshc.getHapCountRank().get(hap).size(); index++) {	// removed ranking
						out.write(globalshc.getHapCountRank().get(hap).get(index) + "\t");	// 
					}
				}
				else {	// haplotype does not exist
					out.write("0\t0\t0\t0\t");	// add 0 except for ranking			
				}
				out.write("\n");				
			}					
			
			for (int count = 0; count < cdl.getDirList().size(); count++) {
				for (String hap : obr.getRankedHapList()) {		// use ordered hap list
					if (hap.contains("~")) {
						continue;
					}
					else {
						out.write(hap + "\t");
					}
					
/*					String [] alleles = hap.split("~");
					for (String allele : alleles) {	// need to handle DRB345					
						if (target.contains("DRB[345]")) {		// target contains DRB345
							if ((hap.contains("DRB3")) || (hap.contains("DRB4")) ||	(hap.contains("DRB5"))) {	
								out.write(allele + "\t");	// hap contains DRB345				
							}
							else {	// hap does not contain DRB345
								if (allele.contains("HLA-DRB1")) {
									out.write("\t" + allele + "t");	// add one column for empty DRB345 before DRB1
								}
								else {
									out.write(allele + "t");	// other allele
								}							
							}									
						}
						else {	// no DRB345 in the target
							out.write(allele + "\t");
						}				
					}	*/	
					out.write(cdl.getDirList().get(count) + "\t");	// country/ethnicity
					
					SampleHapCount groupshc = new SampleHapCount( cdl.getInputFileNameList().get(count), target );
					if (groupshc.getHapCountRank().containsKey(hap)) {
						out.write(String.format("%.6f", groupshc.getFrequency().get(hap)) + "\t");	// frequency
						out.write(groupshc.getHapCountRank().get(hap).get(0) + "\t");	// hap count
						for (int index = 2; index < groupshc.getHapCountRank().get(hap).size(); index++) {	// removed ranking
							out.write(groupshc.getHapCountRank().get(hap).get(index) + "\t");	// 
						}
					}
					else {	// haplotype does not exist
						out.write("0\t0\t0\t0\t");	// add 0 except for ranking		
					}
					out.write("\n");
				}
			}

			out.close();					
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot write in " + output);
		}	
	}

}
