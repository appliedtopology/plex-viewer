package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;

/**
 * This class implements a coloring scheme that assigns a coloring
 * that has uniform energy (2-norm) for each point. In otherwords,
 * it computes a mapping from R^n -> S^2. 
 * 
 * @author Andrew Tausz
 *
 */
public class EqualIntensityColorScheme extends ColorScheme {
	private double saturation = 1;
	
	public double[] computeColor(double[] point) {
		// if we get the origin, then simply return black
		if (DoubleArrayMath.norm(point, 1) == 0) {
			return new double[]{0, 0, 0};
		}
		// compute the standard deformation retract R^3\{0} -> S^2
		return normalize(point, this.saturation);
	}

	private static double[] normalize(double[] point, double targetNorm) {
		double[] result = new double[point.length];
		double multiplier = targetNorm / DoubleArrayMath.norm(point, 2);
		for (int i = 0; i < point.length; i++) {
			result[i] = multiplier * Math.abs(point[i]);
		}
		return result;
	}
}
