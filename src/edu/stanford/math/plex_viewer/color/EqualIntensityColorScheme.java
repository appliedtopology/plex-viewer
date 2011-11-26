package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

/**
 * This class implements a coloring scheme that assigns a coloring
 * that has uniform energy (2-norm) for each point. In other words,
 * it computes a mapping from R^n -> S^2. 
 * 
 * @author Andrew Tausz
 *
 */
public class EqualIntensityColorScheme extends ColorScheme<double[]> {
	private float saturation = 1;
	
	private static EqualIntensityColorScheme instance = new EqualIntensityColorScheme();
	
	public static EqualIntensityColorScheme getInstance() {
		return instance;
	}
	
	public float[] computeColor(double[] point) {
		// if we get the origin, then simply return black
		if (DoubleArrayMath.norm(point, 1) == 0) {
			return new float[]{0, 0, 0};
		}
		// compute the standard deformation retract R^3\{0} -> S^2
		return normalize(resizeArray(point, 3), this.saturation);
	}

	private static float[] normalize(double[] point, float targetNorm) {
		float[] result = new float[point.length];
		double multiplier = targetNorm / DoubleArrayMath.norm(point, 2);
		for (int i = 0; i < point.length; i++) {
			result[i] = (float) (multiplier * Math.abs(point[i]));
		}
		return result;
	}
	
	private static double[] resizeArray(double[] array, int newSize) {
		if (array.length == newSize) {
			return array;
		}
		double[] result = new double[3];
		int n = Math.min(array.length, 3);
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		return result;
	}
}
