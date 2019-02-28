/**
 * create fourth field haplotype, single allele haplotype & two-filed haplotype
 */
package workshop.haplotype.organize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import workshop.haplotype.ambiguity.ConvertToSingleAllele;
import workshop.haplotype.ambiguity.ExtractTwoField;
import workshop.haplotype.family.organize.OrganizeFamilyTrioHapType;
import workshop.haplotype.gene.OrderedHLAgene;

/**
 * @author kazu
 * @version February 28 2019
 *
 */
public class CreateHaplotypeGLstring {
	// 0: full haplotype
	// 1: single haplotype
	// 2: two-filed haplotype
	private List<String> hapList;

	/**
	 * 
	 */
	public CreateHaplotypeGLstring(OrganizeFamilyTrioHapType of, 
			OrderedHLAgene orderedGene, String child, String sample, int index) {
		// TODO Auto-generated constructor stub
		hapList = new ArrayList<String>();
		hapList.addAll(createHap(of.getFamilyTrioHapType(), orderedGene, child, sample, index));
	}
	public List<String> createHap(Map<String, Map<String, Map<String, List<String>>>> familyTrioHapType,
			OrderedHLAgene orderedGene, String child, String sample, int index) {
		String haplotype = "";
		String singleAlleleHaplotype = "";
		String twoFieldHap = "";
		int drb345Count = 0;
		List<String> list = new ArrayList<String>();
		for (String gene : orderedGene.getOrderedGeneList()) {	// 
			if (familyTrioHapType.get(child).containsKey(sample)) {
				if (familyTrioHapType.get(child).get(sample).containsKey(gene)) {
					if (index < familyTrioHapType.get(child).get(sample).get(gene).size()) {								
						if (familyTrioHapType.get(child).get(sample).get(gene).get(index).length() > 0) {
							String type = familyTrioHapType.get(child).get(sample).get(gene).get(index);
							haplotype += type;
							haplotype += "~";	// connect by ~mark	
							
							// let's add HLA-DRB3*ABS
							if ((!type.contains("NT")) && (!type.contains("No"))) {	// skip NT and NoMatch
								ConvertToSingleAllele ctsa = new ConvertToSingleAllele(type);
								if (ctsa.getSingleAllele().contains("HLA-DRB3")) {
									singleAlleleHaplotype += ctsa.getSingleAllele() + "~HLA-DRB4*00:00~HLA-DRB5*00:00~";
//									twoFieldHap += getTwoFieldAllele(type) + "~HLA-DRB4*00:00~HLA-DRB5*00:00~";		
									twoFieldHap += getTwoFieldAllele(ctsa.getSingleAllele()) + "~HLA-DRB4*00:00~HLA-DRB5*00:00~";	
									drb345Count++;
								}
								else if (ctsa.getSingleAllele().contains("HLA-DRB4")) {
									singleAlleleHaplotype += "HLA-DRB3*00:00~" + ctsa.getSingleAllele() + "~HLA-DRB5*00:00~";
//									twoFieldHap += "HLA-DRB3*00:00~" + getTwoFieldAllele(type) + "~HLA-DRB5*00:00~";
									twoFieldHap += "HLA-DRB3*00:00~" + getTwoFieldAllele(ctsa.getSingleAllele()) + "~HLA-DRB5*00:00~";
									drb345Count++;
								}
								else if (ctsa.getSingleAllele().contains("HLA-DRB5")) {
									singleAlleleHaplotype += "HLA-DRB3*00:00~HLA-DRB4*00:00~" + ctsa.getSingleAllele() + "~";
//									twoFieldHap += "HLA-DRB3*00:00~HLA-DRB4*00:00~" + getTwoFieldAllele(type) +  "~";
									twoFieldHap += "HLA-DRB3*00:00~HLA-DRB4*00:00~" + getTwoFieldAllele(ctsa.getSingleAllele()) +  "~";
									drb345Count++;
								}
								else if (ctsa.getSingleAllele().contains("HLA-DRB1")) {
									if (drb345Count == 0) {	// absence of DRB345
										singleAlleleHaplotype += "HLA-DRB3*00:00~HLA-DRB4*00:00~HLA-DRB5*00:00~" 														
												+ ctsa.getSingleAllele() + "~";
//										twoFieldHap += "HLA-DRB3*00:00~HLA-DRB4*00:00~HLA-DRB5*00:00~" 
//												+ getTwoFieldAllele(type) + "~";
										twoFieldHap += "HLA-DRB3*00:00~HLA-DRB4*00:00~HLA-DRB5*00:00~" 
												+ getTwoFieldAllele(ctsa.getSingleAllele()) + "~";
									}
									else {	// presence of DRB345
										singleAlleleHaplotype += ctsa.getSingleAllele() + "~";
	//									twoFieldHap += getTwoFieldAllele(type) + "~";
										twoFieldHap += getTwoFieldAllele(ctsa.getSingleAllele()) + "~";
									}
								}
								else {	// not DRB genes
										singleAlleleHaplotype += ctsa.getSingleAllele() + "~";		
//										twoFieldHap += getTwoFieldAllele(type) + "~";
										twoFieldHap += getTwoFieldAllele(ctsa.getSingleAllele()) + "~";
								}
							}
						}								
					}
				}
			}			
		}
		
		haplotype = haplotype.substring(0, haplotype.length() - 1);	// remove ~ at the end
		list.add(haplotype);
		if (singleAlleleHaplotype.length() > 0) {
			singleAlleleHaplotype = singleAlleleHaplotype.substring(0, singleAlleleHaplotype.length() - 1);
			twoFieldHap = twoFieldHap.substring(0, twoFieldHap.length() -1);
		}
		list.add(singleAlleleHaplotype);
		list.add(twoFieldHap);
		
		return list;
	}
	
	// this function caused ambiguous two field allele
	public String getTwoFieldAllele(String allele) {
		ExtractTwoField extracted = new ExtractTwoField(allele);		// extract two field
		int count = 0;
		String ambiguity = "";
		for (String str : extracted.getExtractedTwoFieldType().getList()) {
			if (count > 0) {
				ambiguity += "/";
			}
			ambiguity += str;
			count++;
		}
		return ambiguity;
	}
	
	public List<String> getHapList() {
		return hapList;
	}


}
