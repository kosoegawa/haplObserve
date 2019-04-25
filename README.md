[![Build Status](https://api.travis-ci.org/mpresteg/haplObserve.svg?branch=master)](https://travis-ci.org/mpresteg/haplObserve)

# Introduction:
HaplObserve builds classical HLA gene haplotypes from genotypes of nuclear families that consist of two parents and at least one child. When one family has multiple children, parental haplotypes are validated.

When multiple families are present in the dataset, haplObserve includes an option to separate the parents by ethnicity or country, and calculates haplotype frequencies per ethnicity or country. Full haplotypes are separated into predefined smaller haplotypes and individual loci, and haplotype or allele frequencies are calculated. Parents are treated as unrelated individuals, while children are not included in the estimation of haplotype frequencies.



# Prerequisite:
 - For Windows computers, we recommend installing the Git Bash terminal (https://git-scm.com/download/win). The Git Bash terminal allows HaplObserve to be run the same way as in Linux and Mac terminals
  
 - Download and Install Java SE Development Kit (JDK - 1.7 or newer)
  
 - Create “&lt;baseDirectory>/collective/” directory structure. The &lt;baseDirectory> name can be anything, such as resource, but “collective” directory name must be used. Do not use space in &lt;baseDirectory> name.
 
# Input files

 - “gl_strings_XXX.csv” files should be stored under “&lt;baseDir>/collective/gl_strings_XXX.csv. The software looks for “gl_strings” to identify files to be used. Multiple families can be included in a single file. If multiple files exist, the software combines them. Note that file name should not contain "combined". HaplObserve does not work if the file name contained "combined".
 - The “gl_strings” file contains the following information: Labcode, Family ID, Sample ID, Relation, Gl String, Ethnicity/Country. These categories should be included in the first line as a header.
 - 
 
 
 
 
 - Alternatively, the software will generate a "gl_strings_XXX.csv" file based upon standard inputs:
     - [.hml](http://bioinformatics.bethematchclinical.org/hla-resources/hml) - standard format for submission of genotype data
     - [.ped](http://valdarlab.unc.edu/pedformat.html) - standard format for expressing pedigree information
     - *INFO*.csv - custom format for expressing correlating Labcode and ethnicity/contry information to an individual.
     - The software looks for "INFO" in file name to identify *INFO*.csv file.
     - The *INFO*.csv file contains: Sample ID, Labcode and ethnicity/country information. Do not include header in *INFO*.csv file.
     - The software will look for individuals across all three of these files and combine the information into the "gl_strings_xxx.csv" format.
     - This option is convenient to build haplotypes from extended or multiple generation families. The software reads "Relationship" information from ".ped" file.



 - Six example of gl_strings files are included (hapl-obs/src/test/resources/collective).
 
 - If HaplObserve builds incorrect haplotypes after initial trial, manually edited haplotypes can be saved in &lt;baseDirectory>/collective/update. The manually edited files is used as final results.
 
 - Newly generated directories should be deleted or moved to other place after each run.
 
# Using the software:

The ability to download the software package and make use of command line tools is available.

 - From the Releases section of GitHub, download one of the snapshots of the latest release.  E.g:  hapl-obs-tools-0.0.1-SNAPSHOT-bin.zip from a given release at [Releases](https://github.com/ihiw/haplObserve/releases)
 - Unzip the software package

After un-zipping the software, test the following commands for instructions on how to run the software:
 - ./hapl-obs-tools-0.0.1-SNAPSHOT/bin/haplotype-driver -h
 - ./hapl-obs-tools-0.0.1-SNAPSHOT/bin/haplotype-table-driver -h
 - Description of these commands can be found below
 
 # haplotype-driver -i &lt;inputFile> -o &lt;outputFile>
 - Takes spreadsheet format input file.
 - This is convenient to build haplotype from a single family
 
 # haplotype-table-driver -b &lt;baseDirectory> -fam
 - Generates haplotypes from multiple families
 - This does NOT calculate haplotype frequencies
  
 # haplotype-table-driver -b &lt;baseDirectory> -full
 - Generates haplotypes from multiple families (same as option1)
 - Separate haplotypes by ethnicity/country
 - Calculates haplotype frequencies for 11 HLA loci (HLA-A, HLA-C, HLA-B, HLA-DRB3/4/5, HLA-DRB1, HLA-DQA1, HLA-DQB1, HLA-DPA1 and HLA-DPB1)
 - Generate summary table that contains haplotype frequencies from all ethnicity/country in a single spreadsheet
 - Generates haplotype frequency table that can be used as reference table for HLAHapV [1]
  
 # haplotype-table-driver -b &lt;baseDirectory> -six
 - Use this when genotypes for HLA-DRB3/4/5, HLA-DQA1 and HLA-DPA1 are not available. 
 - Generates haplotypes from multiple families (same as option1&2)
 - Separate haplotypes by ethnicity/country (same as option2)
 - Calculates haplotype frequencies for 6 HLA loci (HLA-A, HLA-C, HLA-B, HLA-DRB1, HLA-DQB1, HLA-DPB1)
 - Generate summary table that contains haplotype frequencies from all ethnicity/country in a single spreadsheet
 - Generates haplotype frequency table that can be used as reference table for HLAHapV
 
 # haplotype-table-driver -b &lt;baseDirectory> -tdt
 - Use this when performing TDT and multi-allelic TDT from Trio families 
 - Generates transmitted & non-transmitted tables, and input files for TDT R packages
 
# Test / Example Files:
 - Test / example files (which the JUnit tests make use of) can be found at hapl-obs/src/test/resources (csv files and the collective directory)

# (Alternative) Installation and Execution:
 - Install JDK (written for 1.8)
 - Download Maven (https://maven.apache.org/download.cgi)
 - Install Maven (https://maven.apache.org/install.html, or useful instruction for Windows: https://www.mkyong.com/maven/how-to-install-maven-in-windows/) 
 - Confirm correct installation by: mvn -version
 - It is important to set JAVA_HOME and PATH environments correctly.
 
 - Install Git (https://git-scm.com/downloads)
 - Open terminal (use Git Bash terminal for Windows)
 - Change directory where haplObserve should be cloned, e.g., "cd" will change to your home directory
 - Clone the repository: git clone https://github.com/IHIW/haplObserve.git
 - When haploObserve is cloned in home (~): cd haplObserve (go inside of haplObserve directory)
 - From the root of the haplObserve cloned (local) repository: mvn clean install 
 
# References:
1.  K. Osoegawa et al., HLA Haplotype Validator for quality assessments of HLA typing, Hum. Immunol. (2015),
[http://dx.doi.org/10.1016/j.humimm.2015.10.018](http://dx.doi.org/10.1016/j.humimm.2015.10.018)