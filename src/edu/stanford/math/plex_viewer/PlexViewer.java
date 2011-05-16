package edu.stanford.math.plex_viewer;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.io.DoubleArrayReaderWriter;
import edu.stanford.math.plex4.io.SimplexStreamReaderWriter;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

public class PlexViewer {
	// create Options object
	private static Options options = null;

	private static Options createOptions() {
		Options options = new Options();

		// add t option
		options.addOption("complex", true, "File containing filtered simplicial complex");
		options.addOption("points", true, "File containing list of data points");

		Option complex = OptionBuilder.withArgName("file").hasArg().isRequired().withDescription("File contianing filtered simplicial complex").create("complex");
		Option points = OptionBuilder.withArgName("file").hasArg().isRequired().withDescription("File contianing list of data points").create("points");

		options.addOption(complex);
		options.addOption(points);

		return options;
	}

	public static void main(String[] args) {

		options = createOptions();

		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);
			
			String complexFile = line.getOptionValue("complex");
			String pointsFile = line.getOptionValue("points");
			
			renderFromFiles(complexFile, pointsFile);
		}
		catch( ParseException exp ) {
			//System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("plex-viewer", options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void renderFromFiles(String complexFile, String pointsFile) throws IOException {
		AbstractFilteredStream<Simplex> stream = SimplexStreamReaderWriter.getInstance().importFromFile(complexFile);
		double[][] points = DoubleArrayReaderWriter.getInstance().importFromFile(pointsFile);
		
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, points));
		openGLManager.initialize();
	}
}
