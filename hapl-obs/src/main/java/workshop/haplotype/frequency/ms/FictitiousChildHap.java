/**
 * Populate fictitious child (non-transmitted) haplotypes
 */
package workshop.haplotype.frequency.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kazu
 * @version October 7 2018
 *
 */
public class FictitiousChildHap extends SampleHap {

	/**
	 * @param filePath
	 * @param targetLoci
	 */
	public FictitiousChildHap(String filePath, String targetLoci) {
		super(filePath, targetLoci);
		// TODO Auto-generated constructor stub
		
		List<String> tmpSample = new ArrayList<String>();
		Map<String, String> tmpSampleFam = new HashMap<String, String>();
		Map<String, String> tmpSampleRelation = new HashMap<String, String>();
		
		for (String family : familyList.getList()) {	// go through familyList
			List<String> tmpHap = new ArrayList<String>();		// populate non-transmitted haplotypes
			for (String sample : sampleList) {		// go through sampleList
				if (sampleFam.get(sample).equals(family)) {
					if (sampleRelation.get(sample).equals("father")) {
						tmpHap.add(sampleHap.get(sample).get(1));	// paternal non-transmitted haplotype
					}
					else if (sampleRelation.get(sample).equals("mother")) {
						tmpHap.add(sampleHap.get(sample).get(1));	// maternal non-transmitted haplotype
					}					
				}
			}
			String sampleID = family + "_FictionalChild";	// define fictious child id
			sampleHap.put(sampleID, tmpHap);	
			tmpSample.add(sampleID);
			tmpSampleFam.put(sampleID, family);
			tmpSampleRelation.put(sampleID, "fictitious");
		}
		sampleList.addAll(tmpSample);	// add fictitious child in sampleList
		sampleFam.putAll(tmpSampleFam);		// add sample family
		sampleRelation.putAll(tmpSampleRelation);	// add relationship "fictitious"
	}

}
