package edu.stanford.math.plex_viewer;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

/**
 * This class draws geometric realizations of simplicial complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplexStreamViewer implements ObjectRenderer {
	private final GeometricSimplexStream stream;
	private final int maxFiltrationIndex;
	private final float pointSize = 10.0f;
	
	private int currentFiltrationIndex = 0;
	private ColorScheme<Simplex> colorScheme;
	private int maxNumSimplices = 5000;
	private int maxDimension = 10;
	
	/**
	 * This constructor initializes the class with a given GeometricSimplexStream
	 * object.
	 * 
	 * @param geometricSimplexStream the GeometricSimplexStream object to initialize with
	 */
	public SimplexStreamViewer(GeometricSimplexStream geometricSimplexStream) {
		this.stream = geometricSimplexStream;
		this.maxFiltrationIndex = this.stream.getMaximumFiltrationIndex();
		this.colorScheme = new AveragedSimplicialColorScheme<double[]>(this.stream, new HSBColorScheme());
	}
	
	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as a
	 * set of points in Euclidean space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param points the points of the vertices in Euclidean space (should be in R^2 or R^3)
	 */
	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, double[][] points) {
		this(stream, new EuclideanMetricSpace(points));
	}

	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as
	 * a Euclidean metric space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param metricSpace the geometric points in Euclidean space (should be in R^2 or R^3)
	 */
	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
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
        gl.glPointSize(this.pointSize);
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

			if (simplex.getDimension() == 0) {
				this.drawSequence(gl, simplex, GL.GL_POINTS);
			} else if (simplex.getDimension() == 1) {
				this.drawSequence(gl, simplex, GL.GL_LINES);
			} else if (simplex.getDimension() == 2) {
				this.drawSequence(gl, simplex, GL.GL_TRIANGLES);
			}
			simplexCount++;
		}
	}


	/**
	 * This is a helper function which draws the vertices of a given simplex.
	 * 
	 * @param gl the GL object to draw with
	 * @param simplex the simplex to draw
	 * @param glShapeCode the shape code to use
	 */
	private void drawSequence(GL gl, Simplex simplex, int glShapeCode) {
		int[] vertices = simplex.getVertices();
		float[] color = this.colorScheme.computeColor(simplex);
		gl.glBegin(glShapeCode);
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] point = this.stream.getPoint(vertices[vertexIndex]);
			gl.glColor3fv(color, 0);
			if (point.length == 2) {
				gl.glVertex2d(point[0], point[1]);
			} else if (point.length == 3) {
				gl.glVertex3d(point[0], point[1], point[2]);
			}
		}
		gl.glEnd();
	}

	public void processSpecializedKeys(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			this.currentFiltrationIndex += 1;
			this.currentFiltrationIndex = Math.min(this.currentFiltrationIndex, this.maxFiltrationIndex);
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			this.currentFiltrationIndex -= 1;
			this.currentFiltrationIndex = Math.max(0, this.currentFiltrationIndex);
		}
	}
}
