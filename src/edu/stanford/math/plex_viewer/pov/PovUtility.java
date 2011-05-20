package edu.stanford.math.plex_viewer.pov;

import java.io.BufferedWriter;
import java.io.IOException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex_viewer.color.ColorScheme;


public class PovUtility {
	
	protected static void writeSimplex(BufferedWriter writer, Simplex simplex, double[][] points, ColorScheme<Simplex> colorScheme) throws IOException {
		float[] color = colorScheme.computeColor(simplex);
		Texture texture = Texture.getFromRGB(color);
		
		int[] vertices = simplex.getVertices();
		
		if (vertices.length == 1) {
			PovUtility.writeSphere(writer, points[vertices[0]], 0.02, texture);
		} else if (vertices.length == 2) {
			PovUtility.writeCylinder(writer, points[vertices[0]], points[vertices[1]], 0.01, texture);
		} else if (vertices.length == 3) {
			PovUtility.writeTriangle(writer, points[vertices[0]], points[vertices[1]], points[vertices[2]], texture);
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
	
	protected static String toString(double[] array) {
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i]);
		}
		builder.append(">");
		return builder.toString();		
	}
	
	protected static String toString(float[] array) {
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i]);
		}
		builder.append(">");
		return builder.toString();		
	}
	
	protected static String toString(int[] array) {
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i]);
		}
		builder.append(">");
		return builder.toString();		
	}
}
