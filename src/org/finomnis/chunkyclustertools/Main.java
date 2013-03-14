package org.finomnis.chunkyclustertools;

import java.io.File;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Options.parse(args);
		
		if(Options.getOutFile() == null)
		{
			System.out.println("Unable to process, no output file specified!");
			System.exit(1);
		}
		if(Options.getInFiles().length < 1)
		{
			System.out.println("Unable to process, no input files specified!");
			System.exit(1);
		}
		
		String[] inFiles = Options.getInFiles();
		
		
		
		try {
		
			
			
			Dump outDump = new Dump(new File(inFiles[0]));
		
			for(int i = 1; i < inFiles.length; i++)
			{
				outDump.add(new Dump(new File(inFiles[i])));
			}
		
			outDump.writeToDumpFile(new File(Options.getOutFile()));	
		
		
		
		
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		
	}
	
}
