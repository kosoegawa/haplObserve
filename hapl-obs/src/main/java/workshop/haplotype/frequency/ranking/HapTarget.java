/**
 * haplotype target
 */
package workshop.haplotype.frequency.ranking;

import java.util.ArrayList;
import java.util.List;

import workshop.haplotype.gene.HLAgene;

/**
 * @author kazu
 * @version October 12 2018
 *
 */
public class HapTarget {
	private List<String> hapTargetList;
	private List<String> nameList;
	private List<String> hapSixTargetList;
	private List<String> sixNameList;

	/**
	 * 
	 */
	public HapTarget() {
		// TODO Auto-generated constructor stub
		hapTargetList = new ArrayList<String>();
		nameList = new ArrayList<String>();
		hapSixTargetList = new ArrayList<String>();
		sixNameList = new ArrayList<String>();
		hapTargetList.add("HLA-A,HLA-C,HLA-B,DRB[345],DRB1,DQA1,DQB1,DPA1,DPB1");
		nameList.add("Full");
		
		hapTargetList.add("HLA-C,HLA-B,DRB[345],DRB1,DQA1,DQB1,DPA1,DPB1");
		nameList.add("CBDRDQDP");
		
		hapTargetList.add("HLA-A,HLA-C,HLA-B,DRB[345],DRB1,DQA1,DQB1");
		nameList.add("ACBDRDQ");
		
		hapTargetList.add("HLA-C,HLA-B,DRB[345],DRB1,DQA1,DQB1");
		nameList.add("CBDRDQ");
		
		hapTargetList.add("HLA-A,HLA-C,HLA-B,DRB1,DQB1");
		nameList.add("ACBDRB1DQB1");
		hapSixTargetList.add("HLA-A,HLA-C,HLA-B,DRB1,DQB1");
		sixNameList.add("ACBDRB1DQB1");
		
		hapTargetList.add("HLA-C,HLA-B,DRB1,DQB1");
		nameList.add("CBDRB1DQB1");
		hapSixTargetList.add("HLA-C,HLA-B,DRB1,DQB1");
		sixNameList.add("CBDRB1DQB1");
		
		hapTargetList.add("HLA-A,HLA-C,HLA-B");
		nameList.add("ACB");
		hapSixTargetList.add("HLA-A,HLA-C,HLA-B");
		sixNameList.add("ACB");
		
		hapTargetList.add("HLA-C,HLA-B");
		nameList.add("CB");
		hapSixTargetList.add("HLA-C,HLA-B");
		sixNameList.add("CB");
		
		hapTargetList.add("DRB[345],DRB1,DQA1,DQB1,DPA1,DPB1");
		nameList.add("DRDQDP");
		
		hapTargetList.add("DRB[345],DRB1,DQA1,DQB1");
		nameList.add("DRDQ");
		
		hapTargetList.add("DRB[345],DRB1");
		nameList.add("DR");
		
		hapTargetList.add("DQA1,DQB1");
		nameList.add("DQ");
		
		hapTargetList.add("DRB1,DQB1");
		nameList.add("DRB1DQB1");
		hapSixTargetList.add("DRB1,DQB1");
		sixNameList.add("DRB1DQB1");		
		
		hapTargetList.add("DPA1,DPB1");
		nameList.add("DP");
		
		hapTargetList.add("HLA-B,DRB1");
		nameList.add("BDRB1");
		hapSixTargetList.add("HLA-B,DRB1");
		sixNameList.add("BDRB1");
		
		hapTargetList.add("DRB1,DQB1,DPB1");
		nameList.add("DRB1DQB1DPB1");
		hapSixTargetList.add("DRB1,DQB1,DPB1");
		sixNameList.add("DRB1DQB1DPB1");
		
		HLAgene hlaGene = new HLAgene();
		hapTargetList.addAll(hlaGene.getGeneList());
		nameList.addAll(hlaGene.getGeneList());
		
		for (String gene : hlaGene.getGeneList()) {
			if ((gene.contains("DRB3"))) {
				continue;
			}
			else if ((gene.contains("DRB4"))) {
				continue;
			}
			else if ((gene.contains("DRB5"))) {
				continue;
			}
			else if ((gene.contains("DQA1"))) {
				continue;
			}
			else if ((gene.contains("DPA1"))) {
				continue;
			}
			else {
				hapSixTargetList.add(gene);
				sixNameList.add(gene);
			}
		}
	}
	
	public List<String> getHapTargetList() {
		return hapTargetList;		
	}
	
	public List<String> getNameList() {
		return nameList;		
	}
	
	public List<String> getHapSixTargetList() {
		return hapSixTargetList;		
	}
	
	public List<String> getSixNameList() {
		return sixNameList;		
	}

}
