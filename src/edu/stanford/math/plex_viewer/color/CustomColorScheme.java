package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;
import edu.stanford.math.primitivelib.autogen.array.FloatArrayMath;

public class CustomColorScheme extends ColorScheme<double[]> {
	private final double[][] points;
	private final float[][] colors;
	
	public CustomColorScheme(double[][] points, float[][] colors) {
		this.points = points;
		this.colors = colors;
	}
	
	@Override
	public float[] computeColor(double[] point) {
		
		float[] color = new float[3];
		
		for (int i = 0; i < points.length; i++) {
			float factor = (float) Math.exp(-10 * DoubleArrayMath.squaredDistance(point, points[i]));
			FloatArrayMath.accumulate(color, colors[i], factor);
		}
		
		return color;
	}
}
