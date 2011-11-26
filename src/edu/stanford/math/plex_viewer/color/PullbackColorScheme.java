package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.primitivelib.autogen.array.FloatArrayMath;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TObjectDoubleIterator;

/**
 * This class computes the pullback of a color function on the type U to a color
 * function on the type T, through a map T -> U.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the domain type
 * @param <U> the codomain type
 */
public class PullbackColorScheme<T, U> extends ColorScheme<T> {
	private final ColorScheme<U> codomainColorScheme;
	private final DoubleSparseFormalSum<ObjectObjectPair<T, U>> mapping;

	
	
	public PullbackColorScheme(ColorScheme<U> codomainColorScheme, DoubleSparseFormalSum<ObjectObjectPair<T, U>> mapping) {
		this.codomainColorScheme = codomainColorScheme;
		this.mapping = mapping;
	}

	public float[] computeColor(T point) {
		float[] color = new float[3];

		for (TObjectDoubleIterator<ObjectObjectPair<T, U>> iterator = this.mapping.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.key().getFirst().equals(point)) {
				FloatArrayMath.accumulate(color, codomainColorScheme.computeColor(iterator.key().getSecond()), (float) iterator.value());
			}
		}

		return color;
	}
}
