package org.finomnis.chunkyclustertools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Options {

	private static String[] infiles;
	private static String outfile;
	
	public static String[] getInFiles()
	{
		return infiles;
	}

	public static String getOutFile()
	{
		return outfile;
	}
	
	private static void printHelp(org.apache.commons.cli.Options options)
	{
		new HelpFormatter().printHelp("ChunkyClusterTool [-o <file>] <inputfiles>", "\nOptions:", options, "");
		System.exit(2);
	}
	
	@SuppressWarnings("static-access")
	public static void parse(String[] args)
	{
		
		org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
		
		options.addOption(OptionBuilder.hasArg().withArgName("dumpfile").withDescription("write the resulting dump to dumpfile").create('o'));
		//options.addOption(OptionBuilder.hasArg().withArgName("pngfile").withDescription("create png image from dump").withLongOpt("image").create('i'));
		options.addOption("h", "help", false, "print this help message");
		CommandLineParser commandLineParser = new PosixParser();
		
		CommandLine cmd = null;
				
		try {
			cmd = commandLineParser.parse(options, args);
		} catch (ParseException e) {
			printHelp(options);
		}
		
		if (cmd.hasOption('h'))	
			printHelp(options);
		
		infiles = cmd.getArgs();
		if(infiles.length < 1)
			printHelp(options);
		
		if(cmd.hasOption('o'))
			outfile = cmd.getOptionValue('o');
		else
			outfile = null;
		
		
		
	}
	
}
