/**
 * 
 */
package workshop.haplotype.frequency.write;

import java.io.File;

import workshop.haplotype.frequency.ranking.HapTarget;
import workshop.haplotype.write.GenerateFamilyHaplotype;

/**
 * @author kazu
 * @version June 22 2018
 *
 */
public class GenerateSixLociHaplotypeTable extends GenerateFamilyHaplotype {

	/**
	 * @param global
	 */
	public GenerateSixLociHaplotypeTable(String global) {
		super(global);
		// INFO:  CHanging this to the haplotype directory to explicitly separate input files from generated output files - so that output files are not checked into source control
		// TODO: double-check
		//new GenerateCountryHapGLstring(global, 
		//			collective + "FAM_Haplotype_Summary_GL_String_" + today + ".csv", fsrc, "FAM", today);
		
		new GenerateCountryHapGLstring(global, 
				haplotype + "FAM_Haplotype_Summary_GL_String_" + today + ".csv", fsrc, "FAM", today);
		new GenerateCountryHapGLstring(global, 
				haplotype + "SingleAlleleHapType_Haplotype_Summary_GL_String_" + today + ".csv", 
				fsrc, "SingleAllele", today);
		new GenerateCountryHapGLstring(global, 
				haplotype + "TwoFieldAlleleHapType_Haplotype_Summary_GL_String_" + today + ".csv",
				fsrc, "TwoFieldAllele", today);
		
		HapTarget ht = new HapTarget();
		new File(global + "summary").mkdir();	// make FAMCSV dir
		for (int index = 0; index < ht.getHapSixTargetList().size(); index++) {	// go through target	
			System.out.println("Processing: " + ht.getSixNameList().get(index));
			String output = global + "summary/Global_" + ht.getSixNameList().get(index) + "_Haplotype_Summary_" + today + ".csv";
			new GenerateGlobalGroupsHapCountTable(global, 
					ht.getHapSixTargetList().get(index), output);
			
			// HLAHapV format
			new GenerateHapFrequencyTableForHLAHapV(global, ht.getHapSixTargetList().get(index), 
					ht.getSixNameList().get(index), today);
		}
	}

}
