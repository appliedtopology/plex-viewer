package edu.stanford.math.plex_viewer;

/**
 * This interface defines the functionality for a coloring scheme 
 * on Euclidean space. A coloring scheme is simply a function
 * c: R^n -> [0, 1]^3. 
 * 
 * @author Andrew Tausz
 *
 */
public interface ColorScheme {
	
	/**
	 * This function computes the color of a point in R^n.
	 * 
	 * @param point the input point
	 * @return the color of the point according to the coloring scheme
	 */
	public float[] computeColor(double[] point);
}
