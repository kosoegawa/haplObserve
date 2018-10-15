/**
 *  generate input file for multi allelic TDT (mTDT2) R package
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import workshop.haplotype.frequency.ms.SampleHapMatrix;
import workshop.haplotype.frequency.ms.SampleTwoFieldHapMatrix;
import workshop.haplotype.frequency.ranking.HapTarget;

/**
 * @author kazu
 * @version October 12 2018
 *
 */
public class GenerateMatrixFileForMTDT2 extends GenerateInputFileForRpackageTDT {

	/**
	 * @param global
	 */
	public GenerateMatrixFileForMTDT2(String global) {
		super(global);
		// TODO Auto-generated constructor stub
		System.out.println("Processing: GenerateMatrixFileForMTDT2");
		String filePath = haplotype + "FAM_Haplotype_Summary_GL_String_" + today + ".csv";
		HapTarget ht = new HapTarget();		// LOOK INTO HERE
		new File(global + "mTDT").mkdir();	// make summary dir
		new File(global + "mTDT/FULL").mkdir();	// make summary dir
		new File(global + "mTDT/TWOFIELD").mkdir();	// make summary dir
		
		for (int cycle = 0; cycle < ht.getHapTargetList().size(); cycle++) {	// go through target
			String target = ht.getHapTargetList().get(cycle);
			System.out.println("Processing: " + ht.getNameList().get(cycle));			
			SampleHapMatrix shm = new SampleHapMatrix(filePath, target);			
			String label = global + "mTDT/FULL/mTDT_" + ht.getNameList().get(cycle) + "_label.csv";
			String matrix = global + "mTDT/FULL/mTDT_" + ht.getNameList().get(cycle) + "_matrix.csv";
			
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(label));	
				
				for (String hap : shm.getHaplotypes().getList()) {
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
				
				for (int index = 0; index < shm.getHaplotypes().getList().size(); index++) {
					String row = "";
					for (int count = 0; count < shm.getHaplotypes().getList().size(); count++) {
						int test = 0;
						
						for (List<String> list : shm.getListOfHapCombination()) {
							if (shm.getHapCombIndex().get(list).get(0) == index) {
								if (shm.getHapCombIndex().get(list).get(1) == count) {
									row += shm.getHapCombCount().get(list) + ",";
									test++;
								}
							}						
						}
						if (test == 0) {
							row += "0,";
						}
					}

					out.write(row.substring(0, row.length() -1) + "\n");
				}			
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + matrix);
			}
			
			
			// TWOFIELD
			SampleTwoFieldHapMatrix stfhm = new SampleTwoFieldHapMatrix(filePath, target);
			label = global + "mTDT/TWOFIELD/mTDT_" + ht.getNameList().get(cycle) + "_label.csv";
			matrix = global + "mTDT/TWOFIELD/mTDT_" + ht.getNameList().get(cycle) + "_matrix.csv";
			try {
				BufferedWriter out = 
					new BufferedWriter(new FileWriter(label));	
				
				for (String hap : stfhm.getTwoFieldHaplotypes().getList()) {
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
				
				for (int index = 0; index < stfhm.getTwoFieldHaplotypes().getList().size(); index++) {
					String row = "";
					for (int count = 0; count < stfhm.getTwoFieldHaplotypes().getList().size(); count++) {
						int test = 0;
						
						for (List<String> list : stfhm.getListOfTwoFieldHapCombination()) {
							if (stfhm.getTwoFieldHapCombIndex().get(list).get(0) == index) {
								if (stfhm.getTwoFieldHapCombIndex().get(list).get(1) == count) {
									row += stfhm.getTwoFieldHapCombCount().get(list) + ",";
									test++;
								}
							}						
						}
						if (test == 0) {
							row += "0,";
						}
					}

					out.write(row.substring(0, row.length() -1) + "\n");
				}			
				out.close();					
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Cannot write in " + matrix);
			}						
		}				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GenerateMatrixFileForMTDT2("/Users/kazu/Desktop/ussta1_MSTRIO/");

	}

}
