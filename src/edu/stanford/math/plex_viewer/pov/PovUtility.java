package edu.stanford.math.plex_viewer.pov;

public class PovUtility {

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
