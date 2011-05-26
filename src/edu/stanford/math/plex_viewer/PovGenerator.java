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
import edu.stanford.math.plex4.io.FileManager;
import edu.stanford.math.plex4.io.SimplexStreamReaderWriter;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.pov.SimplexStreamPovWriter;

/**
 * This class is implements a command-line interface for generating POV files.
 * 
 * @author Andrew Tausz
 *
 */
public class PovGenerator {
	private static Options options = null;

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();

		Option complex = OptionBuilder.withArgName("file").hasArg().isRequired().withDescription("File containing filtered simplicial complex").create("complex");
		Option points = OptionBuilder.withArgName("file").hasArg().isRequired().withDescription("File containing list of data points").create("points");
		Option output = OptionBuilder.withArgName("file").hasArg().isRequired(false).withDescription("Destination file (if omitted, filename will contain current timestamp)").create("destination");

		options.addOption(complex);
		options.addOption(points);
		options.addOption(output);

		return options;
	}

	public static void main(String[] args) {
		options = createOptions();

		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);
			
			String complexFile = line.getOptionValue("complex");
			String pointsFile = line.getOptionValue("points");
			
			String outputFile = null;
			if (line.hasOption("destination")) {
				outputFile = line.getOptionValue("destination");
			} else {
				outputFile = FileManager.generateUniqueFileName() + ".pov";
			}
			
			renderFromFiles(complexFile, pointsFile, outputFile);
		}
		catch( ParseException exp ) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("plex-viewer", options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void renderFromFiles(String complexFile, String pointsFile, String outputFile) throws IOException {
		AbstractFilteredStream<Simplex> stream = SimplexStreamReaderWriter.getInstance().importFromFile(complexFile);
		double[][] points = DoubleArrayReaderWriter.getInstance().importFromFile(pointsFile);
		
		SimplexStreamPovWriter w = new SimplexStreamPovWriter();

		try {
			w.writeToFile(stream, points, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
