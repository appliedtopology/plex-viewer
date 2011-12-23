package edu.stanford.math.plex_viewer.rendering;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.color.AveragedSimplicialColorScheme;
import edu.stanford.math.plex_viewer.color.ColorScheme;
import edu.stanford.math.plex_viewer.color.HSBColorScheme;
import edu.stanford.math.plex_viewer.gl.GLSettings;
import edu.stanford.math.plex_viewer.gl.GLUtility;

/**
 * This class draws geometric realizations of simplicial complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplexStreamRenderer implements ObjectRenderer {
	protected final GeometricSimplexStream stream;
	protected final int maxFiltrationIndex;
	protected final int minFiltrationIndex;
	
	protected int currentFiltrationIndex = 0;
	protected ColorScheme<Simplex> colorScheme;
	protected int maxNumSimplices = 500000;
	protected int maxDimension = 10;
	
	/**
	 * This constructor initializes the class with a given GeometricSimplexStream
	 * object.
	 * 
	 * @param geometricSimplexStream the GeometricSimplexStream object to initialize with
	 */
	public SimplexStreamRenderer(GeometricSimplexStream geometricSimplexStream) {
		this.stream = geometricSimplexStream;
		this.maxFiltrationIndex = this.stream.getMaximumFiltrationIndex();
		this.minFiltrationIndex = this.stream.getMinimumFiltrationIndex();
		this.colorScheme = new AveragedSimplicialColorScheme<double[]>(this.stream, new HSBColorScheme());
		this.currentFiltrationIndex = this.minFiltrationIndex;
	}
	
	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as a
	 * set of points in Euclidean space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param points the points of the vertices in Euclidean space (should be in R^2 or R^3)
	 */
	public SimplexStreamRenderer(AbstractFilteredStream<Simplex> stream, double[][] points) {
		this(stream, new EuclideanMetricSpace(points));
	}

	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as
	 * a Euclidean metric space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param metricSpace the geometric points in Euclidean space (should be in R^2 or R^3)
	 */
	public SimplexStreamRenderer(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		this(new GeometricSimplexStream(stream, metricSpace));
	}
	
	/**
	 * This function sets the color scheme.
	 * 
	 * @param colorScheme the new color scheme
	 */
	public void setColorScheme(ColorScheme<Simplex> colorScheme) {
		this.colorScheme = colorScheme;
	}
	
	public ColorScheme<Simplex> getColorScheme() {
		return this.colorScheme;
	}

	public void setMaxDimension(int dimension) {
		this.maxDimension = dimension;
	}
	
	public void init(GL gl) {
		gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_POINT_SMOOTH);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glPointSize(GLSettings.defaultPointSize);
	}

	public void renderShape(GL gl) {
		int simplexCount = 0;
		for (Simplex simplex: this.stream) {
			if (simplexCount > this.maxNumSimplices) {
				break;
			}
			
			if (stream.getFiltrationIndex(simplex) > currentFiltrationIndex) {
				break;
			}
			
			if (simplex.getDimension() > maxDimension) {
				continue;
			}

			if (simplex.getDimension() <= 2) {
				GLUtility.drawSimplex(gl, simplex, this.colorScheme, this.stream);
			}
			
			simplexCount++;
		}
	}

	public void processSpecializedKeys(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_8) {
			this.currentFiltrationIndex += 1;
			this.currentFiltrationIndex = Math.min(this.currentFiltrationIndex, this.maxFiltrationIndex);
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
			this.currentFiltrationIndex -= 1;
			this.currentFiltrationIndex = Math.max(this.minFiltrationIndex, this.currentFiltrationIndex);
		}
	}
}
