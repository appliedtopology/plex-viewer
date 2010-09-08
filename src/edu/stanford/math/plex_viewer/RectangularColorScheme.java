package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;


public class RectangularColorScheme implements ColorScheme {

	public double[] computeColor(double[] point) {
		return normalize(point, 0.5);
	}

	private static double[] normalize(double[] point, double targetNorm) {
		double[] result = new double[point.length];
		double multiplier = targetNorm / DoubleArrayMath.norm(point, 2);
		for (int i = 0; i < point.length; i++) {
			result[i] = multiplier * point[i];
		}
		return result;
		
	}
}
