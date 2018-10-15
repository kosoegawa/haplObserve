/**
 * This generate TDT table for haplotypes and individual alleles
 * 
 */
package workshop.haplotype.frequency.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import workshop.haplotype.frequency.ms.HapTDT;
import workshop.haplotype.frequency.ranking.HapTarget;
import workshop.haplotype.write.GenerateFamilyHaplotype;

/**
 * @author kazu
 * @version October 12 2018
 *
 */
public class GenerateHaplotypeTDT extends GenerateFamilyHaplotype {

	/**
	 * @param global
	 */
	public GenerateHaplotypeTDT(String global) {
		super(global);
		// TODO Auto-generated constructor stub
		HapTarget ht = new HapTarget();		// LOOK INTO HERE
		new File(global + "summary").mkdir();	// make summary dir
		for (int count = 0; count < ht.getHapTargetList().size(); count++) {	// go through target	
			System.out.println("Processing: " + ht.getNameList().get(count));			
			String target = ht.getHapTargetList().get(count);
			
			String [] fields = {"FAM", "TwoFieldAllele"};
			for (String field : fields) {
				String output = "";
				if (ht.getNameList().get(count).contains("HLA-")) {	// locus
					output = global + "summary/" + field + "_" + ht.getNameList().get(count) + "_Locus_Summary_" + today + ".csv";
				}
				else {
					output = global + "summary/" + field + "_" + ht.getNameList().get(count) + "_Haplotype_Summary_" + today + ".csv";
				}
				HapTDT tdt = new HapTDT(haplotype + field + "_Haplotype_Summary_GL_String_" + today + ".csv", 
						target);
				
				try {
					BufferedWriter out = 
						new BufferedWriter(new FileWriter(output));	
					
					out.write("Father Count," +tdt.getFatherList().size() + "\n");
					out.write("Mother Count," +tdt.getMotherList().size() + "\n");
					out.write("Child Count," +tdt.getChildList().size() + "\n");
					int combined = tdt.getFatherList().size() + tdt.getMotherList().size() +
							tdt.getChildList().size();
					out.write("Subject Count," + combined + "\n");
					out.write("Father Hap Count," + tdt.getTotalHapCount().get("father") + "\n");
					out.write("Mother Hap Count," + tdt.getTotalHapCount().get("mother") + "\n");
					out.write("Child Hap Count," + tdt.getTotalHapCount().get("child") + "\n");
					for (int index = 0; index < tdt.getLocusCount(); index++) {
						out.write(",");
					}
					for (int index = 0; index < 5; index ++) {
						out.write("Father,");
					}
					for (int index = 0; index < 5; index ++) {
						out.write("Mother,");
					}
					for (int index = 0; index < 4; index ++) {	// 2 => 4
						out.write("Child,");
					}
					for (int index = 0; index < 3; index ++) {	// 0 => 4
						out.write("FictitiousChild,");
					}
					out.write("Parent,");
					out.write("Family,");
					out.write("Sample,");
					out.write("BothParentHomo\n");
					
					for (int index = 0; index < tdt.getLocusCount(); index++) {
						out.write(",");
					}
					String [] second = 
						{"Hap","Frequency","Transmitted","Non-Transmitted","Homo","Hap,Frequency","Transmitted",	
							"Non-Transmitted","Homo","Hap","NonHomo","Homo","Frequency","Hap","NonHomo","Homo",
							"Frequency","Hap","Hap","Hap"};
					for (String element : second) {
						out.write(element + ",");
					}
					out.write("\n");
					
					String [] loci = target.split(",");
					for (String locus : loci) {
						if (!locus.contains("HLA-")) {
							out.write("HLA-" + locus + ",");
						}
						else {
							out.write(locus + ",");
						}
					}

					String [] third = 
						{"Count","%","Count","Count","Count","Count","%","Count","Count","Count",
							"Count", "Count","Count","%", "Count", "Count","Count","%","Count",
							"Count","Count","Chi-Suqare","p-value", "OddsRation", "Lower95%CI", "Upper95%CI"};	
					for (String element : third) {
						out.write(element + ",");
					}
					out.write("\n");
					for (int index = tdt.getOrderedHapByChi().size() -1; index > 0; index--) {
						String hap = tdt.getOrderedHapByChi().get(index);
						
						if (hap.equals("ABS")) {	// skip ABS
							continue;
						}	
						
//						String regex = "HLA-DRB[345]\\*00:00~";
//						String modified = hap.replaceAll(regex, "");
//						String [] alleles = modified.split("~");		// NOT WORKING HERE
						String [] alleles = hap.split("~");		// NOT WORKING HERE
						for (String allele : alleles) {
							if (target.contains("DRB[345]")) {								
//								if ((modified.contains("DRB3")) || (modified.contains("DRB4")) ||
//										(modified.contains("DRB5"))) {
//									out.write(allele + ",");		
//								}
								if ((hap.contains("DRB3")) || (hap.contains("DRB4")) ||
										(hap.contains("DRB5"))) {
									out.write(allele + ",");		
								}
								else {
									if (allele.contains("HLA-DRB1")) {
										out.write("," + allele + ",");
									}
									else {
										out.write(allele + ",");
									}							
								}										
							}
							else {
								out.write(allele + ",");
							}																							
						}											
							
						out.write(tdt.getHapCategorySampleList().get(hap).get("father").size() + ",");					
						out.write(String.format("%.6f", tdt.getHapFrequency().get(hap).get("father")) + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("dadTrans").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("dadNonT").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("dadHomo").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("mother").size() + ",");
						out.write(String.format("%.6f", tdt.getHapFrequency().get(hap).get("mother")) + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("momTrans").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("momNonT").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("momHomo").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("child").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("childNonHomo").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("childHomo").size() + ",");
						out.write(String.format("%.6f", tdt.getHapFrequency().get(hap).get("child")) + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("fictitious").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("ficNonHomo").size() + ",");
						out.write(tdt.getHapCategorySampleList().get(hap).get("ficHomo").size() + ",");
						out.write(String.format("%.6f", tdt.getHapFrequency().get(hap).get("parent")) + ",");
						out.write(tdt.getHapFamilyList().get(hap).size() + ",");
						out.write(tdt.getHapSubjectCount().get(hap) + ",");
						out.write(tdt.getHapHomoFamilyList().get(hap).size() + ",");
						
						if (tdt.getHapChi().containsKey(hap)) {	// chi square
							out.write(String.format("%.6f",tdt.getHapChi().get(hap)) + ",");
						}
						else {
							out.write(",");
						}
						out.write(tdt.getHapPvalue().get(hap) + ",");		// p-value
						for (Double value : tdt.getHapOdds().get(hap)) {					
							out.write(String.format("%.6f", value) + ",");
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

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GenerateHaplotypeTDT("/Users/kazu/Desktop/ussta1_MSTRIO/");

	}

}
