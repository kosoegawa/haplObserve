/**
 * This class generates reports for multiple family haplotypes
 */
package workshop.haplotype.write;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import workshop.haplotype.collective.CombineGLFile;
import workshop.haplotype.collective.FamSamRelationCountry;

/**
 * @author kazu & Matt
 * @version July 5 2018
 *
 */
public class GenerateFamilyHaplotype {
	protected String collective;
	protected String haplotype;	// haplotype dir
	protected String today;
	protected FamSamRelationCountry fsrc;

	/**
	 * 
	 */
	public GenerateFamilyHaplotype(String global) {
		// better handling for directories when trailing slash not provided
		global = global.endsWith("/") ? global : (global + "/");

		collective = global + "collective/"; 
		File file = new File (collective);
		if (!file.exists()) {	// check the presence of collective directory
			System.err.println(file.getAbsolutePath() + " does not exist! Bye!!!");
			System.exit(1);
		}
				
		CombineGLFile combined = new CombineGLFile(collective);
		String famcsv = global + "FAMCSV/";		// decided to create this in global
		haplotype = global + "haplotype/";		// decided to create this in global
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();		
		today = dateFormat.format(date);	
		new RunHaplObserveForMultipleFamilies(collective, combined, famcsv, haplotype, today);
		
		fsrc = new FamSamRelationCountry(combined);		
		
		// generate table
		new GenerateFamSampleRelationHapValidCountryTable(haplotype, "Validation", fsrc,
				"_Haplotype_Summary_Table_" + today + ".csv");				
		new GenerateHaplotypeSummary(haplotype, "Validation", fsrc, today);
		
		// moved to haplotype sub-directory to separate files to be checked in from those that are generated
		new GenerateHaplotypeGLStringSummary(haplotype, haplotype, "Validation", fsrc, today);
		// modified from SingleAlleleHapType to UnambiguousAllele
		new GenerateHaplotypeGLStringSummary(haplotype, haplotype, "UnambiguousAllele", fsrc, today);
		//modified from TwoFieldAlleleHapType to TwoFieldAllele
		new GenerateHaplotypeGLStringSummary(haplotype, haplotype, "TwoFieldAllele", fsrc, today);
		
	}

}
