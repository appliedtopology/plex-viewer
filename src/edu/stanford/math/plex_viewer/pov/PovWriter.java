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

public class PovWriter implements ObjectWriter<GeometricSimplexStream> {
	protected ColorScheme<Simplex> colorScheme = null;
	protected PovScene povScene = new PovScene();
	//protected final double maxFiltrationValue;
	
	//public PovWriter(double maxFiltrationValue) {
	//	this.maxFiltrationValue = maxFiltrationValue;
	//}
	
	@Override
	public String getExtension() {
		return "png";
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
		
		double[][] points = stream.getPoints();

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path, false));

			writer.write(povScene.toString());
			
			for (int i = 0; i < points.length; i++) {
				writePointDeclaration(writer, points[i], "P" + i);
			}
			
			
			for (Simplex simplex: stream) {
				
				writeSimplex(writer, simplex, points, this.colorScheme);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	protected static void writeSimplex(BufferedWriter writer, Simplex simplex, double[][] points, ColorScheme<Simplex> colorScheme) throws IOException {
		float[] color = colorScheme.computeColor(simplex);
		Texture texture = Texture.getFromRGB(color);
		
		int[] vertices = simplex.getVertices();
		
		if (vertices.length == 1) {
			writeSphere(writer, points[vertices[0]], 0.02, texture);
		} else if (vertices.length == 2) {
			writeCylinder(writer, points[vertices[0]], points[vertices[1]], 0.01, texture);
		} else if (vertices.length == 3) {
			writeTriangle(writer, points[vertices[0]], points[vertices[1]], points[vertices[2]], texture);
		}
	}

	protected static void writePointDeclaration(BufferedWriter writer, double[] point, String label) throws IOException {
		// #declare P  = <0.0, 0.0, 0.0>;

		double[] array = new double[3];
		
		for (int i = 0; (i < point.length) && (i < 3); i++) {
			array[i] = point[i];
		}
		
		writer.write("#declare " + label + " = <");
		
		writer.write(array[0] + ", ");
		writer.write(array[1] + ", ");
		writer.write(array[2] + "");
		
		writer.write(">;");
	}
	
	protected static void writePoint(BufferedWriter writer, double[] point) throws IOException {
		// <0.0, 0.0, 0.0>

		double[] array = new double[3];
		
		for (int i = 0; (i < point.length) && (i < 3); i++) {
			array[i] = point[i];
		}
		
		writer.write("<");
		
		writer.write(array[0] + ", ");
		writer.write(array[1] + ", ");
		writer.write(Double.toString(array[2]));
		
		writer.write(">");
	}
	
	protected static void writeTriangle(BufferedWriter writer, double[] p1, double[] p2, double[] p3, Texture texture) throws IOException {
		writer.write("triangle {");
		
		writePoint(writer, p1);
		writer.write(",");
		writePoint(writer, p2);
		writer.write(",");
		writePoint(writer, p3);
		
		writer.write(texture.toString());
		
		writer.write("}");
	}
	
	protected static void writeCylinder(BufferedWriter writer, double[] p1, double[] p2, double radius, Texture texture) throws IOException {
		writer.write("cylinder {");
		
		writePoint(writer, p1);
		writer.write(",");
		writePoint(writer, p2);
		writer.write(",");
		writer.write(radius + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");
	}
	
	protected static void writeSphere(BufferedWriter writer, double[] p1,  double radius, Texture texture) throws IOException {
		writer.write("sphere {");
		
		writePoint(writer, p1);
		writer.write(",");
		writer.write(radius + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");	
	}
	
}
