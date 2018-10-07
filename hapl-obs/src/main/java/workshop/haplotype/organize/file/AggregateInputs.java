package workshop.haplotype.organize.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import workshop.haplotype.family.IndividualInfo;
import workshop.haplotype.family.Parents;
import workshop.haplotype.family.Pedigree;

public class AggregateInputs {
	private static final String HML = "hml";
	private static final String XML = "xml";
	private static final String PED = "ped";
	private static final String CSV = "csv";
	private static final String GENE_DELIMITER = "^";
	private static final String SAMPLE_ELEMENT = "sample";
	private static final String ID_ATTRIBUTE = "id";
	private static final String TYPING_ELEMENT = "typing";
	private static final String ALLELE_ASSIGNMENT_ELEMENT = "allele-assignment";
	private static final String GL_STRING_ELEMENT = "glstring";
	
	private static final String FILE_DELIMITER_REGEX = "[\t,\\s+]";
	private static final String CSV_DELIMITER_REGEX = "[,]";
	
	private static final int PED_FAMILY_ID = 0;
	private static final int PED_SUBJECT_ID = 1;
	private static final int PED_PATERNAL_ID = 2;
	private static final int PED_MATERNAL_ID = 3;
	
	private static final int INFO_SUBJECDT_ID = 0;
	private static final int INFO_LAB_CODE = 1;
	private static final int INFO_ETHNICITY = 2;
	
	private static final String MOTHER = "mother";
	private static final String FATHER = "father";
	private static final String CHILD = "child";
	private static final String INFO = "INFO";
	
	public void organizeFiles(String baseDir) {
		// Establish the base directory
		baseDir = baseDir.endsWith("/") ? baseDir : (baseDir + "/");
		File collective = new File(baseDir + "collective");
		
		// Look for hml, ped and supplemental information files according to filters
		File[] hmlFiles = collective.listFiles(new HMLFileFilter());
		File[] pedFiles = collective.listFiles(new PEDFileFilter());
		File[] individualInfoFiles = collective.listFiles(new InfoFileFilter());
		
		// Initialize maps to hold information found in files
		HashMap<String, String> glStringMap = new HashMap<String, String>();
		HashMap<String, Pedigree> pedigreeMap = new HashMap<String, Pedigree>();
		HashMap<String, IndividualInfo> individualInfoMap = new HashMap<String, IndividualInfo>();
		
		// Loop through the various files found and load into maps
		for (File hmlFile : hmlFiles) {
			glStringMap.putAll(getGLString(hmlFile));
		}
		
		for (File pedFile : pedFiles) {
			pedigreeMap.putAll(getPedigree(pedFile));
		}
		
		for (File individualInfoFile : individualInfoFiles) {
			individualInfoMap.putAll(getIndividualInfo(individualInfoFile));
		}
		
		// Ensure convenient 'natural' ordering of the records encountered in the ped file(s)
		TreeSet<String> individualsByGeneration = new TreeSet<String>(pedigreeMap.keySet());
		
		// Prepare to write file
		FileWriter writer;
		boolean write = false;
		
		StringBuffer output = new StringBuffer("Labcode,Famly Id,Sampleid,Relation,Gl String,Country\n");
		try {
			// loop through the individuals identified in the ped files according to generation of the family
			for (String individualByGeneration : individualsByGeneration) {
				// Get the individuals pedigree in the context of their generation
				Pedigree pedigree = pedigreeMap.get(individualByGeneration);
				
				// Check to see if a gl string was provided (hml) for the individual
				if (glStringMap.containsKey(pedigree.getIndividualId())) {
					
					// Get the supplemental info (race, lab code according to the individual
					IndividualInfo individualInfo = individualInfoMap.get(pedigree.getIndividualId());
					
					// Format and write the output
					output.append(individualInfo.getLabCode() + "," + pedigree.getFamilyGenerationId() + "," + pedigree.getIndividualId() + "," +
							pedigree.getRelation() + "," + glStringMap.get(pedigree.getIndividualId()) + "," + individualInfo.getEthnicity() + "\n");
					write = true;			
				}
			}
			
			if (write) {
				writer = new FileWriter(collective.getPath() + "/gl_strings_Constructed_Test.csv");
				writer.write(output.toString());
				
				writer.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Pedigree> getPedigree(File pedFile) {
		HashMap<String, Pedigree> pedigreeMap = new HashMap<String, Pedigree>();
		List<Parents> parentSet = new ArrayList<Parents>();
		
		BufferedReader reader = null;
		String line;
		String[] parts;
		Pedigree pedigree = null;
		Parents parents = null;
		
		try {
			// Read the ped file
			reader = new BufferedReader(new FileReader(pedFile));
			
			while ((line = reader.readLine()) != null) {
				parts = line.split(FILE_DELIMITER_REGEX);
				
				// establish record for the child
				pedigree = new Pedigree();
				
				pedigree.setIndividualId(parts[PED_SUBJECT_ID]);
				pedigree.setRelation(CHILD);
				
				// establish generations by identifying parents by family
				parents = new Parents();
				
				parents.setFamilyId(parts[PED_FAMILY_ID]);
				parents.setPaternalId(parts[PED_PATERNAL_ID]);
				parents.setMaternalId(parts[PED_MATERNAL_ID]);
				
				// assuming this same set of parents has not been encountered before, add them to the set
				if (!parentSet.contains(parents)) parentSet.add(parents);
							
				// establish a generational index by combining family with a unique set of parents within that extended family
				String familyGenerationId = parents.getFamilyId() + "_" + (parentSet.indexOf(parents));
				pedigree.setFamilyGenerationId(familyGenerationId);
				
				// put the record for the child into the proper family generation
				pedigreeMap.put(familyGenerationId + pedigree.getIndividualId(), pedigree);
				
				// establish record for the father
				pedigree = new Pedigree();
				
				pedigree.setFamilyGenerationId(familyGenerationId);
				pedigree.setIndividualId(parts[PED_PATERNAL_ID]);
				pedigree.setRelation(FATHER);
				
				// put the record for the father into the proper family generation
				pedigreeMap.put(familyGenerationId + pedigree.getIndividualId(), pedigree);
				
				// establish record for the mother
				pedigree = new Pedigree();
				
				pedigree.setFamilyGenerationId(familyGenerationId);
				pedigree.setIndividualId(parts[PED_MATERNAL_ID]);
				pedigree.setRelation(MOTHER);
				
				// put the record for the mother into the proper family generation
				pedigreeMap.put(familyGenerationId + pedigree.getIndividualId(), pedigree);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return pedigreeMap;
	}
	
	private HashMap<String, IndividualInfo> getIndividualInfo(File individualInfoFile) {
		HashMap<String, IndividualInfo> individualInfoMap = new HashMap<String, IndividualInfo>();
		
		BufferedReader reader = null;
		String line;
		String[] parts;
		
		try {
			// read the supplemental info file
			reader = new BufferedReader(new FileReader(individualInfoFile));
			
			while ((line = reader.readLine()) != null) {
				// split on comma
				parts = line.split(CSV_DELIMITER_REGEX);
				
				// put the supplemental info into a map, according to individual
				IndividualInfo individualInfo = new IndividualInfo();
				individualInfo.setLabCode(parts[INFO_LAB_CODE]);
				individualInfo.setEthnicity(parts[INFO_ETHNICITY]);
				individualInfoMap.put(parts[INFO_SUBJECDT_ID], individualInfo);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return individualInfoMap;
	}
	
	private HashMap<String, String> getGLString(File hmlFile) {
		HashMap<String, String> glStringMap = new HashMap<String, String>();
		
		BufferedReader reader = null;
		try {
			// read the hml file
			reader = new BufferedReader(new FileReader(hmlFile));
		
			// load / parse the xml document
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(reader);
		    Document doc = builder.parse(is);
		    
		    String sampleId;
		    Element alleleAssignment;
	
		    // Find and loop through the sample element(s)
		    NodeList nList = doc.getElementsByTagName(SAMPLE_ELEMENT);
		    for (int i=0;i<nList.getLength();i++) {
		    	// Get the id for the sample
		    	sampleId = nList.item(i).getAttributes().getNamedItem(ID_ATTRIBUTE).getNodeValue();
			    
		    	StringBuffer glString = new StringBuffer();
			    
			    // Find and loop through the typing element(s)
		    	NodeList typingElements = ((Element) nList.item(i)).getElementsByTagName(TYPING_ELEMENT);
		    	for (int j=0;j<typingElements.getLength();j++) {
		    		// Grab the allele assignment element
		    		alleleAssignment = (Element) ((Element) typingElements.item(j)).getElementsByTagName(ALLELE_ASSIGNMENT_ELEMENT).item(0);
		    		
		    		// incorporate a gene delimiter between typing elements
		    		if (j > 0) glString.append(GENE_DELIMITER);
		    		
		    		// build multi-locus gl string
		    		glString.append(((Element) alleleAssignment.getElementsByTagName(GL_STRING_ELEMENT).item(0)).getTextContent().trim());
		    	}
		    	
		    	// put the gl string into a map according to individual
		    	glStringMap.put(sampleId, glString.toString());
		    }
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return glStringMap;
		
	}
	
	private class HMLFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(HML) || name.toLowerCase().endsWith(XML)) return true;
			
			return false;
		}
		
	}
	
	private class PEDFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(PED)) return true;
			
			return false;
		}
	}
	
	private class InfoFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			if (name.contains(INFO) && name.endsWith(CSV)) return true;
			
			return false;
		}
	}
}
