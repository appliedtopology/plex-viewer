package edu.stanford.math.plex_viewer;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.array.FloatArrayMath;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;

public class SimplexStreamViewer implements ObjectRenderer {
	private final GeometricSimplexStream stream;
	private final int maxFiltrationIndex;
	private final float pointSize = 10.0f;
	
	private int currentFiltrationIndex = 0;
	private ColorScheme colorSheme = new EqualIntensityColorScheme();
	private int maxNumSimplices = 2000;
	
	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, double[][] points) {
		this(stream, new EuclideanMetricSpace(points));
	}

	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		this.stream = new GeometricSimplexStream(stream, metricSpace);
		this.maxFiltrationIndex = stream.getMaximumFiltrationIndex();
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


	private void drawSequence(GL gl, Simplex simplex, int glShapeCode) {
		int[] vertices = simplex.getVertices();
		float[] color = this.computeColor(simplex);
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

	private float[] computeColor(Simplex simplex) {
		float[] rgb = new float[3];

		int[] vertices = simplex.getVertices();
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] vertexPoint = this.stream.getPoint(vertices[vertexIndex]);
			FloatArrayMath.accumulate(rgb, this.colorSheme.computeColor(vertexPoint));
		}

		FloatArrayMath.inPlaceMultiply(rgb, 1.0f / (float) vertices.length);

		return rgb;
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
