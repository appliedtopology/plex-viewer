/**
 * 
 */
package edu.stanford.math.plex_viewer;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;
import edu.stanford.math.plex4.functional.GenericFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.utility.MetricUtility;
import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class SimplexStreamViewer implements ObjectRenderer {
	protected GeometricSimplexStream stream;
	
	protected double maxFiltrationValue = 0;
	protected final double delta;
	protected final int dimension;
	protected final int numPoints;
	
	protected final float pointSize = 5.0f;
	protected double[] empericalMeans;
	protected double[] meanShift;
	protected final ColorScheme colorScheme;
	
	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, FiniteMetricSpace<double[]> metricSpace) {
		this.stream = new GeometricSimplexStream(stream, metricSpace);
		this.dimension = metricSpace.getPoint(0).length;
		this.numPoints = metricSpace.size();
		this.empericalMeans = MetricUtility.computeMeans(metricSpace);
		this.delta = 0.1;
		this.meanShift = new double[this.dimension];
		this.colorScheme = new EqualIntensityColorScheme();
	}
	
	public void setColorFunction(GenericFunction<Simplex, double[]> colorFunction) {
		ExceptionUtility.verifyNonNull(colorFunction);
	}
	
	public void renderShape(GL gl) {
		for (Simplex simplex: this.stream) {
			double filtrationValue = this.stream.getFiltrationValue(simplex);
			
			if (filtrationValue > this.maxFiltrationValue) {
				break;
			}

			if (simplex.getDimension() == 0) {
				this.drawSequence(gl, simplex, GL.GL_POINTS);
			} else if (simplex.getDimension() == 1) {
				this.drawSequence(gl, simplex, GL.GL_LINES);
			} else if (simplex.getDimension() == 2) {
				this.drawSequence(gl, simplex, GL.GL_TRIANGLES);
			}
		}
	}

	private void drawSequence(GL gl, Simplex simplex, int glShapeCode) {
		int[] vertices = simplex.getVertices();
		gl.glBegin(glShapeCode);
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] point = this.stream.getPoint(vertices[vertexIndex]);
			gl.glColor3dv(this.computeSimplexColor(simplex), 0);
			double[] shiftedPoint = this.meanCenterPoint(point);
			if (point.length == 2) {
				gl.glVertex2d(shiftedPoint[0], shiftedPoint[1]);
			} else if (point.length == 3) {
				gl.glVertex3d(shiftedPoint[0], shiftedPoint[1], shiftedPoint[2]);
			}
		}
		gl.glEnd();
	}
	
	/**
	 * This function computes the color of a simplex by calculating the average
	 * color of its vertices.
	 * 
	 * @param simplex the simplex to compute the color of
	 * @return the color of the simplex
	 */
	private double[] computeSimplexColor(Simplex simplex) {
		double[] color = new double[3];
		int[] vertices = simplex.getVertices();
		
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] vertexColor = this.colorScheme.computeColor(this.stream.getPoint(vertices[vertexIndex]));
			DoubleArrayMath.accumulate(color, vertexColor);
		}
		
		DoubleArrayMath.inPlaceMultiply(color, 1.0 / vertices.length);
		return color;

	}
	
	/**
	 * This function processes the keystrokes for stepping through the
	 * filtration.
	 */
	public void processSpecializedKeys(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			this.maxFiltrationValue += delta;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			this.maxFiltrationValue -= delta;
			this.maxFiltrationValue = Math.max(0, this.maxFiltrationValue);
		}
	}
	
	public void setMeanShift(double[] shift) {
		ExceptionUtility.verifyEqual(this.dimension, shift.length);
		this.meanShift = shift;
	}
	
	private double[] meanCenterPoint(double[] point) {
		double[] result = new double[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = point[i] - this.empericalMeans[i] + this.meanShift[i];
		}
		return result;
	}

	public void init(GL gl) {
		gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glEnable(GL.GL_POINT_SMOOTH);
	    gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glPointSize(this.pointSize);
	    
	    //gl.glGetFloatv(GL.GL_POINT_SIZE_MAX_EXT, pmax, 0);

	    //gl.glPointParameterfvEXT(GL.GL_DISTANCE_ATTENUATION_EXT, linear, 0);
	    //gl.glPointParameterfEXT(GL.GL_POINT_FADE_THRESHOLD_SIZE_EXT, 2.0f);
	}
}
