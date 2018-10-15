/**
 * need to run HaplObserveExtendedTest to generate FAM_Haplotype_Summary_GL_String file
 * before testing
 */
package workshop.haplotype;

import java.util.List;

import org.junit.Test;

import workshop.haplotype.frequency.ms.FictitiousChildHap;
import workshop.haplotype.frequency.ms.HapCount;
import workshop.haplotype.frequency.ms.HapFrequency;
import workshop.haplotype.frequency.ms.SampleHap;
import workshop.haplotype.frequency.ms.SampleHapCountForRpackageTDT;
import workshop.haplotype.frequency.ms.SampleHapMatrix;
import workshop.haplotype.frequency.ms.SampleTwoFieldHap;
import workshop.haplotype.frequency.ms.SampleTwoFieldHapMatrix;
import workshop.haplotype.frequency.ranking.OrderbyRanking;
import workshop.haplotype.frequency.ranking.SampleHapCount;

/**
 * @author kazu
 * @version June 21 2018
 *
 */
public class RankingTest {
	private static final String global = "src/test/resources/";
	private static final String target = "HLA-C,HLA-B";
//	private static final String target = "DRB[345],HLA-DRB1,HLA-DQB1";
	private static String input = global + "FAM_Haplotype_Summary_GL_String_TEST.csv";
	private static String trio = global + "FAM_Haplotype_Summary_GL_String_TRIO.csv";
	
	@Test
	public void testSampleHapCountForRpackageTDT() {
		System.out.println("SampleHapCountForRpackageTDT");
		SampleHapCountForRpackageTDT sh = new SampleHapCountForRpackageTDT(trio, target);
		
		System.out.println("Full allele name");
		for (String hap : sh.getHaplotypes().getList()) {	
			System.out.println(hap);
		}
		
		String header = ",";
		for (int index = 0; index < sh.getHaplotypes().getList().size(); index++ ) {
			header += Integer.toString(index+1) + ",";
		}
		System.out.println(header.substring(0, header.length() - 1) );
		
		for (String family : sh.getFamilyList().getList()) {
			for (String relation : sh.getRelation()) {
				for (String sample : sh.getFamSampleFullHapCount().get(family).keySet()) {
					if (sh.getSampleRelation().get(sample).equals(relation)) {
						String str = sample + ",";
						for (String hap : sh.getHaplotypes().getList()) {								
							str += sh.getFamSampleFullHapCount().get(family).get(sample).get(hap) + ",";								
						}
						System.out.println(str.substring(0, str.length() - 1));
					}
				}
				System.out.println();
			}				
		}
		
		System.out.println("Two-field allele name");
		for (String hap : sh.getTwoFieldHaplotypes().getList()) {	
			System.out.println(hap);
		}
		
		header = ",";
		for (int index = 0; index < sh.getTwoFieldHaplotypes().getList().size(); index++ ) {
			header += Integer.toString(index+1) + ",";
		}
		System.out.println(header.substring(0, header.length() - 1));
		
		for (String family : sh.getFamilyList().getList()) {
			for (String relation : sh.getRelation()) {
				for (String sample : sh.getFamSampleTwoFieldHapCount().get(family).keySet()) {
					if (sh.getSampleRelation().get(sample).equals(relation)) {
						String str = sample + ",";
						for (String hap : sh.getTwoFieldHaplotypes().getList()) {								
							str += sh.getFamSampleTwoFieldHapCount().get(family).get(sample).get(hap) + ",";								
						}
						System.out.println(str.substring(0, str.length() - 1));
					}
				}
				System.out.println();
			}				
		}		
		System.out.println();
	}
	
	
	@Test
	public void testSampleTwoFieldHapMatrix() {
		System.out.println("SampleTwoFieldHapMatrix");

		SampleTwoFieldHapMatrix matrix = new SampleTwoFieldHapMatrix(trio, target);
		
		for (String hap : matrix.getTwoFieldHaplotypes().getList()) {
			System.out.println(hap);
		}	
		
		for (int index = 0; index < matrix.getTwoFieldHaplotypes().getList().size(); index++) {
			String row = "";
			for (int count = 0; count < matrix.getTwoFieldHaplotypes().getList().size(); count++) {
				int test = 0;
				
				for (List<String> list : matrix.getListOfTwoFieldHapCombination()) {
					if (matrix.getTwoFieldHapCombIndex().get(list).get(0) == index) {
						if (matrix.getTwoFieldHapCombIndex().get(list).get(1) == count) {
							row += matrix.getTwoFieldHapCombCount().get(list) + ",";
							test++;
						}
					}						
				}
				if (test == 0) {
					row += "0,";
				}
			}

			System.out.println(row.substring(0, row.length() -1));
		}
		System.out.println();
	}
	
	
	@Test
	public void testSampleHapMatrix() {
		System.out.println("SampleHapMatrix");
		SampleHapMatrix matrix = new SampleHapMatrix(trio, target);	
		
		for (String hap : matrix.getHaplotypes().getList()) {
			System.out.println(hap);
		}
		
		for (int index = 0; index < matrix.getHaplotypes().getList().size(); index++) {
			String row = "";
			for (int count = 0; count < matrix.getHaplotypes().getList().size(); count++) {
				int testCount = 0;
				
				for (List<String> list : matrix.getListOfHapCombination()) {
					if (matrix.getHapCombIndex().get(list).get(0) == index) {
						if (matrix.getHapCombIndex().get(list).get(1) == count) {
							row += matrix.getHapCombCount().get(list) + ",";
							testCount++;
						}
					}						
				}
				if (testCount == 0) {
					row += "0,";
				}
			}
			System.out.println(row.substring(0, row.length() -1));
		}				
		System.out.println();
	}
	
	
	@Test
	public void testSampleTwoFieldHap() {
		System.out.println("SampleTwoFieldHap");

		SampleTwoFieldHap stfh = new SampleTwoFieldHap(trio, target);
		for (String hap : stfh.getTwoFieldHaplotypes().getList()) {
			System.out.println(hap);
		}
		
		for (String sample : stfh.getSampleTwoFieldHap().keySet()) {
			System.out.println(sample);
			for (String hap : stfh.getSampleTwoFieldHap().get(sample)) {
				System.out.println(hap);
			}
		}
		System.out.println();
	}
	
	
	@Test
	public void testHapFrequency() {
		System.out.println("HapFrequency");
		
		HapFrequency hf = new HapFrequency(trio, target);
		
		for (String haplotype : hf.getHaplotypes().getList()) {
			System.out.println(haplotype);
			System.out.println("father: " + hf.getHapCategorySampleList().get(haplotype).get("father").size());					
			System.out.println(String.format("%.6f", hf.getHapFrequency().get(haplotype).get("father")));
			System.out.println("dadTrans: " + hf.getHapCategorySampleList().get(haplotype).get("dadTrans").size());
			System.out.println("dadNonT: " + hf.getHapCategorySampleList().get(haplotype).get("dadNonT").size());
			System.out.println("dadHomo: " + hf.getHapCategorySampleList().get(haplotype).get("dadHomo").size());
			System.out.println("mother: " + hf.getHapCategorySampleList().get(haplotype).get("mother").size());					
			System.out.println(String.format("%.6f", hf.getHapFrequency().get(haplotype).get("mother")));
			System.out.println("momTrans: " + hf.getHapCategorySampleList().get(haplotype).get("momTrans").size());
			System.out.println("momNonT: " + hf.getHapCategorySampleList().get(haplotype).get("momNonT").size());
			System.out.println("momHomo: " + hf.getHapCategorySampleList().get(haplotype).get("momHomo").size());
			System.out.println("child: " + hf.getHapCategorySampleList().get(haplotype).get("child").size());
			System.out.println("childNonHomo: " + hf.getHapCategorySampleList().get(haplotype).get("childNonHomo").size());
			System.out.println("childHomo: " + hf.getHapCategorySampleList().get(haplotype).get("childHomo").size());
			System.out.println(String.format("%.6f", hf.getHapFrequency().get(haplotype).get("child")));
			System.out.println("fictitious: " + hf.getHapCategorySampleList().get(haplotype).get("fictitious").size());
			System.out.println("ficNonHomo: " + hf.getHapCategorySampleList().get(haplotype).get("ficNonHomo").size());
			System.out.println("ficHomo: " + hf.getHapCategorySampleList().get(haplotype).get("ficHomo").size());
			System.out.println(String.format("%.6f", hf.getHapFrequency().get(haplotype).get("parent")));
			System.out.println();
		}
		
		System.out.println();
	}
	
	
	@Test
	public void testHapCount() {
		System.out.println("HapCount");
		
		HapCount hc = new HapCount(trio, target);
		for (String haplotype : hc.getHaplotypes().getList()) {
			if (haplotype.contains("HLA-C*04:01:01:01")) {
//			if (haplotype.contains("HLA-DRB1*15:01:01:01")) {
				System.out.println(haplotype);
				System.out.println(hc.getTotalHapCount());
				for (String category : hc.getCategory()) {
					System.out.println(category + "count:" + hc.getHapCategorySampleList().get(haplotype).get(category).size());
				}
				double a = hc.getHapCategorySampleList().get(haplotype).get("childNonHomo").size()
						+ hc.getHapCategorySampleList().get(haplotype).get("childHomo").size();
				double b = hc.getTotalHapCount().get("child")/2 - a;
				double c = hc.getHapCategorySampleList().get(haplotype).get("ficNonHomo").size()
						+ hc.getHapCategorySampleList().get(haplotype).get("ficHomo").size();
				double d = hc.getTotalHapCount().get("child")/2 - c;
				double odds = (a*d)/(b*c);
				double max = Math.exp((Math.log(odds) + 1.96*Math.sqrt(1/a+1/b+1/c+1/d)));
				double min = Math.exp((Math.log(odds) - 1.96*Math.sqrt(1/a+1/b+1/c+1/d)));
				System.out.println(a);
				System.out.println(b);
				System.out.println(c);
				System.out.println(d);
				System.out.println(odds);
				System.out.println(max);
				System.out.println(min);
			}			
		}		
		System.out.println();
	}
	
	
	@Test
	public void testFictitiousChildHap() {
		System.out.println("FictitiousChildHap");
//		String target = "HLA-B,HLA-C";
//		String target = "DRB[345],HLA-DRB1,HLA-DQB1";
		FictitiousChildHap fc = new FictitiousChildHap(trio, target);
				
		for (String sample : fc.getSampleRelation().keySet()) {
			if (fc.getSampleRelation().get(sample).equals("fictitious")) {
				System.out.println(sample);
				for (String hap : fc.getSampleHap().get(sample)) {
					System.out.println(hap);
				}
			}
		}
		System.out.println();
	}
	

	@Test
	public void testSampleHap() {
		
		System.out.println("SampleHap");
		SampleHap sh = new SampleHap(input, target);
		for (int index = 0; index < sh.getHaplotypes().getList().size(); index++) {
			System.out.println(index + ": " + sh.getHaplotypes().getList().get(index));
		}
		System.out.println();
	}
	
	
	@Test
	public void testSampleHapCount() {
		System.out.println("SampleHapCount");
		SampleHapCount shc = new SampleHapCount(input, target);
		int index = 0;
		for (String hap : shc.getHapCountRank().keySet()) {
			System.out.println(index + ": " + hap + " => " + shc.getHapCountRank().get(hap).get(0) + "\t" +
					shc.getHapCountRank().get(hap).get(1));
			index++;
		}
		System.out.println();
		
		index = 0;
		System.out.println("OrderbyRanking");
		OrderbyRanking or = new OrderbyRanking(shc);
		for (String hap : or.getRankedHapList()) {
			System.out.println(index + ": " + hap + " => "  + shc.getHapCountRank().get(hap).get(0) + "\t" 
					+ shc.getHapCountRank().get(hap).get(1));
			index++;
		}
		System.out.println();			
	}

}
