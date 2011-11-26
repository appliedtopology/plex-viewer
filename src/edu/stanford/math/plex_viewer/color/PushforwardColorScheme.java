package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.primitivelib.autogen.array.FloatArrayMath;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TObjectDoubleIterator;

/**
 * This class computes the pushforward of a color function on the type T to a color
 * function on the type U, through a map T -> U.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the domain type
 * @param <U> the codomain type
 */
public class PushforwardColorScheme<T, U> extends ColorScheme<U> {
	private final ColorScheme<T> domainColorScheme;
	private final DoubleSparseFormalSum<ObjectObjectPair<T, U>> mapping;

	public PushforwardColorScheme(ColorScheme<T> domainColorScheme, DoubleSparseFormalSum<ObjectObjectPair<T, U>> mapping) {
		this.domainColorScheme = domainColorScheme;
		this.mapping = mapping;
	}

	public float[] computeColor(U point) {
		float[] color = new float[3];

		for (TObjectDoubleIterator<ObjectObjectPair<T, U>> iterator = this.mapping.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.key().getSecond().equals(point)) {
				FloatArrayMath.accumulate(color, domainColorScheme.computeColor(iterator.key().getFirst()), (float) iterator.value());
			}
		}

		return color;
	}
}
