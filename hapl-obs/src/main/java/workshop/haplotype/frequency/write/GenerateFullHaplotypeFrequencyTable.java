/**
 * 
 */
package workshop.haplotype.frequency.write;

import java.io.File;

import workshop.haplotype.frequency.ranking.HapTarget;
import workshop.haplotype.write.GenerateFamilyHaplotype;

/**
 * @author kazu/Matt
 * @version July 3 2018
 *
 */
public class GenerateFullHaplotypeFrequencyTable extends GenerateFamilyHaplotype {

	/**
	 * @param global
	 */
	public GenerateFullHaplotypeFrequencyTable(String global) {
		super(global);
		// INFO:  Changing this to the haplotype directory to explicitly separate input files from generated output files - so that output files are not checked into source control
		// TODO: double-check
		
		// better handling for directories when trailing slash not provided
		global = global.endsWith("/") ? global : (global + "/");
		
		new GenerateCountryHapGLstring(global, 
				haplotype + "FAM_Haplotype_Summary_GL_String_" + today + ".csv", fsrc, "FAM", today);
		new GenerateCountryHapGLstring(global, 
				haplotype + "UnambiguousAllele_Haplotype_Summary_GL_String_" + today + ".csv", 
				fsrc, "UnambiguousAllele", today);
		new GenerateCountryHapGLstring(global, 
				haplotype + "TwoFieldAllele_Haplotype_Summary_GL_String_" + today + ".csv",
				fsrc, "TwoFieldAllele", today);
		
		HapTarget ht = new HapTarget();		// LOOK INTO HERE
		new File(global + "summary").mkdir();	// make summary dir
		for (int index = 0; index < ht.getHapTargetList().size(); index++) {	// go through target	
			System.out.println("Processing: " + ht.getNameList().get(index));
			String output = "";
			if (ht.getNameList().get(index).contains("HLA-")) {	// locus
				output = global + "summary/Global_" + ht.getNameList().get(index) + "_Locus_Summary_" + today + ".csv";
			}
			else {
				output = global + "summary/Global_" + ht.getNameList().get(index) + "_Haplotype_Summary_" + today + ".csv";
			}
			
			new GenerateGlobalGroupsHapCountTable(global, 
					ht.getHapTargetList().get(index), output);
			
			if (ht.getNameList().get(index).contains("HLA-")) {	// locus
				output = global + "summary/Global_" + ht.getNameList().get(index) + "_Locus_Summary_" + today + ".tsv";
			}
			else {
				output = global + "summary/Global_" + ht.getNameList().get(index) + "_Haplotype_Summary_" + today + ".tsv";
			}
			
			new GenerateGlobalHapTSVTable(global, 
					ht.getHapTargetList().get(index), output);
			
			// HLAHapV format
			new GenerateHapFrequencyTableForHLAHapV(global, ht.getHapTargetList().get(index), 
					ht.getNameList().get(index), today);
		}
	}

}
