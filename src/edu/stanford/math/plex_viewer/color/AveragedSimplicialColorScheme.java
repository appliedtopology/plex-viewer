package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.primitivelib.autogen.array.FloatArrayMath;

/**
 * This class computes the color of a geometric realization of a simplex but averaging
 * the colors of its vertices. The colors of its vertices are determined by a color scheme
 * on the underlying metric space.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type of the underlying metric space (e.g. double[])
 */
public class AveragedSimplicialColorScheme<T> extends ColorScheme<Simplex> {
	private final AbstractObjectMetricSpace<T> metricSpace;
	private final ColorScheme<T>  geometricColorScheme;

	public AveragedSimplicialColorScheme(AbstractObjectMetricSpace<T> metricSpace, ColorScheme<T>  geometricColorScheme) {
		this.metricSpace = metricSpace;
		this.geometricColorScheme = geometricColorScheme;
	}
	
	public float[] computeColor(Simplex simplex) {
		float[] rgb = new float[3];

		int[] vertices = simplex.getVertices();
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			T vertexPoint = this.metricSpace.getPoint(vertices[vertexIndex]);
			FloatArrayMath.accumulate(rgb, this.geometricColorScheme.computeColor(vertexPoint));
		}

		FloatArrayMath.inPlaceMultiply(rgb, 1.0f / (float) vertices.length);

		return rgb;
	}
}
