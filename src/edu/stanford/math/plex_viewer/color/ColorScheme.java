package edu.stanford.math.plex_viewer.color;

/**
 * This interface defines the functionality for a coloring scheme 
 * on Euclidean space. A coloring scheme is simply a function
 * c: R^n -> [0, 1]^3. 
 * 
 * @author Andrew Tausz
 *
 */
public abstract class ColorScheme<T> {
	
	/**
	 * This function computes the color of a point in R^n.
	 * 
	 * @param point the input point
	 * @return the color of the point according to the coloring scheme
	 */
	public abstract float[] computeColor(T point);
	
	public float[][] computeColor(T[] points) {
		float[][] colors = new float[points.length][3];
		
		for (int i = 0; i < points.length; i++) {
			float[] color = this.computeColor(points[i]);
			
			for (int j = 0; j < 3; j++) {
				colors[i][j] = color[j];
			}
		}
		
		return colors;
	}
}
