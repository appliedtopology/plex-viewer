package edu.stanford.math.plex_viewer;

/**
 * This interface defines the functionality for a coloring scheme 
 * on Euclidean space. A coloring scheme is simply a function
 * c: R^n -> [0, 1]^3. 
 * 
 * @author Andrew Tausz
 *
 */
public abstract class ColorScheme {
	
	/**
	 * This function computes the color of a point in R^n.
	 * 
	 * @param point the input point
	 * @return the color of the point according to the coloring scheme
	 */
	abstract double[] computeColor(double[] point);
	
	double[] convertTo3Vector(double[] vector) {
		double[] result = new double[3];
		
		return result;
	}
}
