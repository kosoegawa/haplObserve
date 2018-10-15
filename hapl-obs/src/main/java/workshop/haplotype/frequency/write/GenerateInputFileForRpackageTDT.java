/**
 *  write input file for standard TDT R package
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import workshop.haplotype.frequency.ms.SampleHapCountForRpackageTDT;
import workshop.haplotype.frequency.ranking.HapTarget;

/**
 * @author kazu
 * @version October 12 2018
 *
 */
public class GenerateInputFileForRpackageTDT extends GenerateSignificantTDTHaplotype {

	/**
	 * @param global
	 */
	public GenerateInputFileForRpackageTDT(String global) {
		super(global);
		// TODO Auto-generated constructor stub
		System.out.println("Processing: GenerateInputFileForRpackageTDT");
		String filePath = haplotype + "FAM_Haplotype_Summary_GL_String_" + today + ".csv";
		HapTarget ht = new HapTarget();		// LOOK INTO HERE
		new File(global + "TDT").mkdir();	// make summary dir
		new File(global + "TDT/FULL").mkdir();	// make summary dir
		new File(global + "TDT/TWOFIELD").mkdir();	// make summary dir
		
		for (int count = 0; count < ht.getHapTargetList().size(); count++) {	// go through target
			String target = ht.getHapTargetList().get(count);
			System.out.println("Processing: " + ht.getNameList().get(count));
			SampleHapCountForRpackageTDT sh = new SampleHapCountForRpackageTDT(filePath, target);
			String label = global + "TDT/FULL/TDT_" + ht.getNameList().get(count) + "_label.csv";
			String matrix = global + "TDT/FULL/TDT_" + ht.getNameList().get(count) + "_matrix.csv";
			
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(label));	

				for (String hap : sh.getHaplotypes().getList()) {	
					out.write(hap + "\n");
				}
								
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + label);
			}
			
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(matrix));		
				String header = ",";
				for (int index = 0; index < sh.getHaplotypes().getList().size(); index++ ) {
					header += Integer.toString(index+1) + ",";
				}

				out.write(header.substring(0, header.length() - 1) + "\n");
				
				for (String family : sh.getFamilyList().getList()) {	// go through family
					for (String relation : sh.getRelation()) {	// father, mother, child
						for (String sample : sh.getFamSampleFullHapCount().get(family).keySet()) {
							if (sh.getSampleRelation().get(sample).equals(relation)) {
								String str = sample + ",";
								for (String hap : sh.getHaplotypes().getList()) {								
									str += sh.getFamSampleFullHapCount().get(family).get(sample).get(hap) + ",";								
								}
								out.write(str.substring(0, str.length() - 1));
							}
						}
						out.write("\n");
					}				
				}
				
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + matrix);
			}
			
			// TWOFIELD
			label = global + "TDT/TWOFIELD/TDT_" + ht.getNameList().get(count) + "_label.csv";
			matrix = global + "TDT/TWOFIELD/TDT_" + ht.getNameList().get(count) + "_matrix.csv";
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(label));	

				for (String hap : sh.getTwoFieldHaplotypes().getList()) {	
					out.write(hap + "\n");
				}
								
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + label);
			}
			
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(matrix));		

				String header = ",";
				for (int index = 0; index < sh.getTwoFieldHaplotypes().getList().size(); index++ ) {
					header += Integer.toString(index+1) + ",";
				}
				out.write(header.substring(0, header.length() - 1) + "\n");
				
				for (String family : sh.getFamilyList().getList()) {
					for (String relation : sh.getRelation()) {
						for (String sample : sh.getFamSampleTwoFieldHapCount().get(family).keySet()) {
							if (sh.getSampleRelation().get(sample).equals(relation)) {
								String str = sample + ",";
								for (String hap : sh.getTwoFieldHaplotypes().getList()) {								
									str += sh.getFamSampleTwoFieldHapCount().get(family).get(sample).get(hap) + ",";								
								}
								out.write(str.substring(0, str.length() - 1));
							}
						}
						out.write("\n");
					}				
				}
				
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + matrix);
			}
		}
		System.out.println("DONE!");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GenerateInputFileForRpackageTDT("/Users/kazu/Desktop/ussta1_MSTRIO/");

	}

}
