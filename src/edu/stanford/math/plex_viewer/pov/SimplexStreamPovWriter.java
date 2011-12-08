package edu.stanford.math.plex_viewer.pov;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.io.ObjectWriter;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.color.AveragedSimplicialColorScheme;
import edu.stanford.math.plex_viewer.color.ColorScheme;
import edu.stanford.math.plex_viewer.color.HSBColorScheme;

/**
 * This class generates a POV file which can be read by povray to produce a ray-traced image
 * of a geometric simplicial complex.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplexStreamPovWriter implements ObjectWriter<GeometricSimplexStream> {
	protected ColorScheme<Simplex> colorScheme = null;
	protected PovScene povScene = new PovScene();

	@Override
	public String getExtension() {
		return "png";
	}

	public void setColorScheme(ColorScheme<Simplex> colorScheme) {
		this.colorScheme = colorScheme;
	}
	
	public ColorScheme<Simplex> getColorScheme() {
		return this.colorScheme;
	}
	
	public void writeToFile(AbstractFilteredStream<Simplex> stream, double[][] points, String path, ColorScheme<Simplex> colorScheme) throws IOException {
		writeToFile(stream, new EuclideanMetricSpace(points), path, colorScheme);
	}
	
	public void writeToFile(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace, String path, ColorScheme<Simplex> colorScheme) throws IOException {
		writeToFile(new GeometricSimplexStream(stream, metricSpace), path, colorScheme);
	}
	
	
	public void writeToFile(AbstractFilteredStream<Simplex> stream, double[][] points, String path) throws IOException {
		writeToFile(stream, new EuclideanMetricSpace(points), path);
	}
	
	public void writeToFile(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace, String path) throws IOException {
		writeToFile(new GeometricSimplexStream(stream, metricSpace), path);
	}
	
	@Override
	public void writeToFile(GeometricSimplexStream stream, String path) throws IOException {
		this.colorScheme = new AveragedSimplicialColorScheme<double[]>(stream, new HSBColorScheme());
		writeToFile(stream, path, this.colorScheme);
	}
	
	public void writeToFile(GeometricSimplexStream stream, String path, ColorScheme<Simplex> colorScheme) throws IOException {	
		double[][] points = stream.getPoints();

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path, false));

			writer.write(povScene.toString());
			PovUtility.writeDefaults(writer);
			
			for (int i = 0; i < points.length; i++) {
				PovUtility.writePointDeclaration(writer, points[i], PovUtility.getPointLabel(i));
			}
			
			
			for (Simplex simplex: stream) {
				PovUtility.writeSimplex(writer, simplex, points, colorScheme);
				writer.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
