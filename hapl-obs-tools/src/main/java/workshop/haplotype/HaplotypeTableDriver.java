/*

    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package workshop.haplotype;

import java.util.concurrent.Callable;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.StringArgument;

import workshop.haplotype.frequency.write.GenerateFullHaplotypeFrequencyTable;
import workshop.haplotype.frequency.write.GenerateMatrixFileForMTDT2;
import workshop.haplotype.frequency.write.GenerateSixLociHaplotypeTable;
import workshop.haplotype.organize.file.AggregateInputs;
import workshop.haplotype.write.GenerateFamilyHaplotype;

/**
 * AnalyzeGLStrings
 *
 */
public class HaplotypeTableDriver implements Callable<Integer> {
	
    private final String baseDir;
    private static Switch family;
    private static Switch full;
    private static Switch six;
    private static Switch tdt;		// added by Kazu

    private static final String USAGE = "haplotype-table-driver [args]";


    /**
     * Analyze gl string using linkage disequilibrium frequencies
     *
     * @param inputFile input file, if any
     * @param outputFile output interpretation file, if any
     */
    public HaplotypeTableDriver(String baseDir) {
        this.baseDir = baseDir;
    }
    
    @Override
    public Integer call() throws Exception { 
    	AggregateInputs aggregateInputs = new AggregateInputs();
    	aggregateInputs.organizeFiles(baseDir);
    	
    	if (family.wasFound()) {
    		new GenerateFamilyHaplotype(baseDir);
    	}
    	else if (full.wasFound()) {
    		new GenerateFullHaplotypeFrequencyTable(baseDir);
    	}
    	else if (six.wasFound()) {
    		new GenerateSixLociHaplotypeTable(baseDir);
    	}
    	else if (tdt.wasFound()) {	// added by Kazu
    		new GenerateMatrixFileForMTDT2(baseDir);
    	}
    	return 0;
    }

    /**
     * Main.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        Switch about = new Switch("a", "about", "display about message");
        Switch help  = new Switch("h", "help", "display help message");
        StringArgument baseDir = new StringArgument("b", "baseDir", "base directory, default current directory", false);
        family = new Switch("fam", "family", "Generates haplotypes, but not frequencies, from multiple families");
        full = new Switch("full", "full", "Generates haplotypes and 11 locus frequencies, from multiple families");
        six = new Switch("six", "six", "Generates haplotypes and 6 locus frequencies, from multiple families");
        // Kazu added
        tdt = new Switch("tdt", "tdt", "Generates transmitted & non-transmitted tables, and input files for TDTand multi-allelic TDT R packages");

        ArgumentList arguments  = new ArgumentList(about, help, baseDir, family, full, six, tdt);
        CommandLine commandLine = new CommandLine(args);

        HaplotypeTableDriver haplotypeTableDriver = null;
        try
        {
            CommandLineParser.parse(commandLine, arguments);
            
            haplotypeTableDriver = (baseDir.getValue() == null) ? new HaplotypeTableDriver(System.getProperty("user.dir")) : new HaplotypeTableDriver(baseDir.getValue());
            
            if (about.wasFound()) {
                haplotypeTableDriver.new AboutHaplotypeTableDriver().about(System.out);
                System.exit(0);
            }
            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
                System.exit(0);
            }
        }
        catch (CommandLineParseException | IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        try {
            System.exit(haplotypeTableDriver.call());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private class AboutHaplotypeTableDriver extends About {
        @Override
        public String addText() {
            StringBuilder sb = new StringBuilder();

            sb.append("fam:  Generates haplotypes, but not frequencies, from multiple families\n"); 
            sb.append("full: Generates haplotypes and 11 locus frequencies, from multiple families\n" + 
            		" - Separate haplotypes by ethnicity/country\n" + 
            		" - Calculates haplotype frequencies for 11 HLA loci (HLA-A, HLA-C, HLA-B, HLA-DRB3/4/5, HLA-DRB1, HLA-DQA1, HLA-DQB1, HLA-DPA1 and HLA-DPB1)\n" + 
            		" - Generate summary table that contains haplotype frequencies from all ethnicity/country in a single spreadsheet\n" + 
            		" - Generates haplotype frequency table that can be used as reference table for HLAHapV.\n");
            sb.append("six:  Generates haplotypes and 6 locus frequencies, from multiple families\n" +
					" - Use this when genotypes for HLA-DRB3/4/5, HLA-DQA1 and HLA-DPA1 are not available. \n" + 
					" - Generates haplotypes from multiple families\n" + 
					" - Separate haplotypes by ethnicity/country\n" + 
					" - Calculates haplotype frequencies for 6 HLA loci (HLA-A, HLA-C, HLA-B, HLA-DRB1, HLA-DQB1, HLA-DPB1)\n" + 
					" - Generate summary table that contains haplotype frequencies from all ethnicity/country in a single spreadsheet\n" + 
					" - Generates haplotype frequency table that can be used as reference table for HLAHapV\n\n");
            sb.append("tdt: Generates transmitted & non-transmitted tables, and input files for TDT R packages\n" +
					" - Use this when performing TDT and multi-allelic TDT from Trio families\n" + 
            		" - Generates transmitted & non-transmitted haplotype table\n" +
					" - Generates input files for Standard TDT R package\n" + 
            		" - Generates input files for multi-allelic TDT R package\n");
            return sb.toString();
        }
    }
    
}
