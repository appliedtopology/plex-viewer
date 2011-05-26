package edu.stanford.math.plex_viewer.pov;

import java.io.BufferedWriter;
import java.io.IOException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex_viewer.color.ColorScheme;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;


public class PovUtility {
	public static final float[] defaultPointColor = new float[]{0.2f, 0.2f, 0.2f};
	public static final float[] defaultBackgroundColor = new float[]{1.0f, 1.0f, 1.0f};
	public static final double defaultSphereRadius = 0.02;
	public static final double defaultCylinderRadius = 0.01;
	public static final double defaultBoxRadius = 0.03;
	
	public static final String sphereRadiusLabel = "r_sphere";
	public static final String cylinderRadiusLabel = "r_cylinder";
	public static final String boxRadiusLabel = "r_box";
	public static final String pointColorLabel = "default_point_color";
	
	protected static void writeDefaults(BufferedWriter writer) throws IOException {
		writeValueDeclaration(writer, defaultSphereRadius, sphereRadiusLabel);
		writeValueDeclaration(writer, defaultCylinderRadius, cylinderRadiusLabel);
		writeValueDeclaration(writer, defaultBoxRadius, boxRadiusLabel);
		writePointDeclaration(writer, defaultPointColor, pointColorLabel);
	}
	
	protected static String getPointLabel(int index) {
		return "P" + index;
	}
	
	protected static void writeSimplex(BufferedWriter writer, Simplex simplex, double[][] points, ColorScheme<Simplex> colorScheme) throws IOException {
		float[] color = colorScheme.computeColor(simplex);
		Texture texture = Texture.getFromRGB(color);
		
		int[] vertices = simplex.getVertices();
		
		if (vertices.length == 1) {
			PovUtility.writeSphere(writer, vertices[0], texture);
		} else if (vertices.length == 2) {
			PovUtility.writeCylinder(writer, vertices[0], vertices[1], texture);
		} else if (vertices.length == 3) {
			PovUtility.writeTriangle(writer, vertices[0], vertices[1], vertices[2], texture);
		}
	}
	
	protected static void writePointDeclaration(BufferedWriter writer, double[] point, String label) throws IOException {
		// #declare P  = <0.0, 0.0, 0.0>;
		writer.write(String.format("#declare %s = %s;", label, PovUtility.toPovString(point)));
		writer.newLine();
	}
	
	protected static void writePointDeclaration(BufferedWriter writer, float[] point, String label) throws IOException {
		// #declare P  = <0.0, 0.0, 0.0>;
		writer.write(String.format("#declare %s = %s;", label, PovUtility.toPovString(point)));
		writer.newLine();
	}
	
	protected static void writeValueDeclaration(BufferedWriter writer, double value, String label) throws IOException {
		// #declare P  = 0.0;
		writer.write(String.format("#declare %s = %f;", label, value));
		writer.newLine();
	}
	
	protected static void writeValueDeclaration(BufferedWriter writer, float value, String label) throws IOException {
		// #declare P  = 0.0;
		writer.write(String.format("#declare %s = %f;", label, value));
		writer.newLine();
	}
	
	protected static void writePoint(BufferedWriter writer, double[] point) throws IOException {
		// <0.0, 0.0, 0.0>
		writer.write(PovUtility.toPovString(point));
	}
	
	protected static void writePoint(BufferedWriter writer, float[] point) throws IOException {
		// <0.0, 0.0, 0.0>
		writer.write(PovUtility.toPovString(point));
	}
	
	protected static void writeTriangle(BufferedWriter writer, double[] p1, double[] p2, double[] p3, Texture texture) throws IOException {
		writer.write("triangle {");
		
		writePoint(writer, fitToLength(p1, 3));
		writer.write(", ");
		writePoint(writer, fitToLength(p2, 3));
		writer.write(", ");
		writePoint(writer, fitToLength(p3, 3));
		writer.write(" ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeCylinder(BufferedWriter writer, double[] p1, double[] p2, Texture texture) throws IOException {
		writer.write("cylinder {");
		
		writePoint(writer, fitToLength(p1, 3));
		writer.write(", ");
		writePoint(writer, fitToLength(p2, 3));
		writer.write(",");
		writer.write(cylinderRadiusLabel + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeSphere(BufferedWriter writer, double[] p1, Texture texture) throws IOException {
		writer.write("sphere {");
		
		writePoint(writer, fitToLength(p1, 3));
		writer.write(", ");
		writer.write(sphereRadiusLabel + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeBox(BufferedWriter writer, double[] p1, Texture texture) throws IOException {
		writer.write("box {");
		
		writePoint(writer, DoubleArrayMath.scalarAdd(fitToLength(p1, 3), defaultBoxRadius));
		writer.write(", ");
		writePoint(writer, DoubleArrayMath.scalarAdd(fitToLength(p1, 3), -defaultBoxRadius));
		writer.write(" ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeTriangle(BufferedWriter writer, int p1, int p2, int p3, Texture texture) throws IOException {
		writer.write("triangle {");
		
		writer.write(getPointLabel(p1));
		writer.write(", ");
		writer.write(getPointLabel(p2));
		writer.write(", ");
		writer.write(getPointLabel(p3));;
		writer.write(" ");
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeCylinder(BufferedWriter writer, int p1, int p2, Texture texture) throws IOException {
		writer.write("cylinder {");
		
		writer.write(getPointLabel(p1));
		writer.write(", ");
		writer.write(getPointLabel(p2));
		writer.write(", ");
		writer.write(cylinderRadiusLabel + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeSphere(BufferedWriter writer, int p1, Texture texture) throws IOException {
		writer.write("sphere {");
		
		writer.write(getPointLabel(p1));
		writer.write(", ");
		writer.write(sphereRadiusLabel + " ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}
	
	protected static void writeBox(BufferedWriter writer, int p1, Texture texture) throws IOException {
		writer.write("box {");
		
		writer.write(getPointLabel(p1) + " + " + boxRadiusLabel);
		writer.write(", ");
		writer.write(getPointLabel(p1) + " - " + boxRadiusLabel);
		writer.write(" ");
		
		writer.write(texture.toString());
		
		writer.write("}");
		writer.newLine();
	}

	protected static double[] fitToLength(double[] array, int length) {
		if (array.length == length) {
			return array;
		}
		
		double[] result = new double[length];
		int n = Math.min(array.length, length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	protected static float[] fitToLength(float[] array, int length) {
		if (array.length == length) {
			return array;
		}
		
		float[] result = new float[length];
		int n = Math.min(array.length, length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	protected static String toPovString(double[] array) {
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(String.format("%f", array[i]));
		}
		builder.append(">");
		return builder.toString();		
	}
	
	protected static String toPovString(float[] array) {
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
	
	protected static String toPovString(int[] array) {
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
