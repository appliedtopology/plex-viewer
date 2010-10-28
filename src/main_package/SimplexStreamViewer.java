/**
 * 
 */
package main_package;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.functional.GenericFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.Infinity;

/**
 * @author Andrew Tausz
 *
 */
public class SimplexStreamViewer implements ObjectRenderer {
	protected GeometricSimplexStream stream;

	protected double maxFiltrationValue = 0;
	protected final double delta;
	protected final double diameter;
	protected final int dimension;
	protected final int numPoints;

	protected final float pointSize = 10.0f;

	protected final double[] maxima;
	protected final double[] minima;
	protected final double[] empericalMeans;
	protected double[] meanShift;

	protected GenericFunction<Simplex, double[]> colorFunction;

	public SimplexStreamViewer(AbstractFilteredStream<Simplex> stream, FiniteMetricSpace<double[]> metricSpace) {
		this.stream = new GeometricSimplexStream(stream, metricSpace);
		this.dimension = metricSpace.getPoint(0).length;
		this.numPoints = metricSpace.size();
		this.maxima = this.computeMaxima(metricSpace);
		this.minima = this.computeMinima(metricSpace);
		this.empericalMeans = this.computeMeans(metricSpace);
		this.diameter = this.computeMaxDiameter();
		this.delta = this.diameter / 100;
		this.meanShift = new double[this.dimension];
		this.colorFunction = this.getDefaultColorFunction();
	}

	public void setColorFunction(GenericFunction<Simplex, double[]> colorFunction) {
		ExceptionUtility.verifyNonNull(colorFunction);
		this.colorFunction = colorFunction;
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
			gl.glColor3dv(this.colorFunction.evaluate(simplex), 0);
			double[] shiftedPoint = this.meanCenterPoint(point);
			if (point.length == 2) {
				gl.glVertex2d(shiftedPoint[0], shiftedPoint[1]);
			} else if (point.length == 3) {
				gl.glVertex3d(shiftedPoint[0], shiftedPoint[1], shiftedPoint[2]);
			}
		}
		gl.glEnd();
	}

	/*
	private void colorVertex(GL gl, double[] point) {
		double[] normalizedPoint = this.normalizePoint(point);
		if (point.length == 2) {
			gl.glColor3d(normalizedPoint[0], normalizedPoint[1], 0);
		} else if (point.length == 3) {
			gl.glColor3d(normalizedPoint[0], normalizedPoint[1], normalizedPoint[2]);
		}
	}
	 */

	private double[] defaultColorFunction(int[] vertices) {
		double[] color = new double[3];

		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] normalizedPoint = this.normalizePoint(this.stream.getPoint(vertices[vertexIndex]));
			//double[] normalizedPoint = this.getSphericalColor(this.stream.getPoint(vertices[vertexIndex]));
			for (int pointIndex = 0; pointIndex < normalizedPoint.length; pointIndex++) {
				color[2 - pointIndex] += normalizedPoint[pointIndex] + 0.1;
			}
		}

		for (int pointIndex = 0; pointIndex < color.length; pointIndex++) {
			color[pointIndex] /= vertices.length;
		}

		return color;
	}

	private double[] defaultColorFunction(Simplex simplex) {
		return this.defaultColorFunction(simplex.getVertices());
	}

	public GenericFunction<Simplex, double[]> getDefaultColorFunction() {
		return new GenericFunction<Simplex, double[]>() {

			public double[] evaluate(Simplex argument) {
				return trigColorFunction(argument);
				//return getSphericalColor(argument);
			}

		};
	}

	public double[] getSphericalColor(Simplex simplex) {
		return this.sphericalColorFunction(simplex.getVertices());
	}

	private double[] sphericalColorFunction(int[] vertices) {
		double[] color = new double[3];

		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] normalizedPoint = (this.stream.getPoint(vertices[vertexIndex]));
			double[] sphericalColor = this.getSphericalColor(normalizedPoint);
			//sphericalColor[1] /= (Math.PI / 1);
			//double[] normalizedPoint = this.getSphericalColor(this.stream.getPoint(vertices[vertexIndex]));
			for (int pointIndex = 0; pointIndex < sphericalColor.length; pointIndex++) {
				color[2 - pointIndex] += 0.8 * sphericalColor[pointIndex];
			}
		}

		for (int pointIndex = 0; pointIndex < color.length; pointIndex++) {
			color[pointIndex] /= vertices.length;
		}

		return color;
	}

	public double[] getSphericalColor(double[] point) {
		double[] sphericalCoordinates = this.toSphericalCoordinates(point);
		sphericalCoordinates[2] = (sphericalCoordinates[2] + Math.PI) / (2 * Math.PI);
		sphericalCoordinates[1] = (sphericalCoordinates[1]) / (2 * Math.PI);
		return sphericalCoordinates;
	}

	private double[] toSphericalCoordinates(double[] point) {
		double[] sphericalCoordinates = new double[3];
		double[] p = new double[3];
		for (int i = 0; i < point.length; i++) {
			p[i] = point[i];
		}
		sphericalCoordinates[0] = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
		sphericalCoordinates[1] = Math.acos(p[2] / sphericalCoordinates[0]);
		sphericalCoordinates[2] = Math.atan2(p[1], p[0]);
		return sphericalCoordinates;
	}

	public double[] trigColorFunction(Simplex simplex) {
		return this.trigColorFunction(simplex.getVertices());
	}

	private double[] trigColorFunction(int[] vertices) {
		double[] color = new double[3];

		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] point = this.stream.getPoint(vertices[vertexIndex]);
			double[] rgb = new double[3];

			if (point.length == 2) {
				double theta = Math.atan2(point[1], point[0]);
				double r = point[0]*point[0] + point[1]*point[1];
				
				double h = theta * (180 / Math.PI) % 360;
				if (h < 0) {
					h += 360;
				}
				double s = 1;
				double v = 0.9;
				convertHSVtoRGB(h, s, v, rgb);
			}
			else {
				//double[] normalizedPoint = this.normalizePoint(this.stream.getPoint(vertices[vertexIndex]));
				double[] normalizedPoint = this.toSphericalCoordinates(this.stream.getPoint(vertices[vertexIndex]));

				double h = ((int)(normalizedPoint[2] * (180 / Math.PI)) + (int)(normalizedPoint[1] * (180 / Math.PI))) % 360;
				double s = 0.5 + 0.25 * (1 + Math.cos(normalizedPoint[0]));
				double v = 0.5 + 0.25 * (1 + Math.sin(normalizedPoint[0]));
				convertHSVtoRGB(h, s, v, rgb);
			}
			for (int pointIndex = 0; pointIndex < rgb.length; pointIndex++) {
				color[pointIndex] += rgb[pointIndex];
			}
		}

		double norm = color[0]*color[0] + color[1]*color[1] + color[2]*color[2];

		for (int pointIndex = 0; pointIndex < color.length; pointIndex++) {
			color[pointIndex] /= vertices.length;
		}

		return color;
	}

	public static void convertHSVtoRGB(double h, double s, double v, double[] rgb) {
		double r = 0;
		double g = 0;
		double b = 0;

		if (s == 0) {
			// this color in on the black white center line <=> h = UNDEFINED
			if (Double.isNaN(h)) {
				// Achromatic color, there is no hue
				r = v;
				g = v;
				b = v;
			} else {
				return;
			}
		} else {
			if (h == 360) {
				// 360 is equiv to 0
				h = 0;
			}

			// h is now in [0,6)
			h = h / 60;

			int i = (int) Math.floor(h);
			double f = h - i; //f is fractional part of h
			double p = v * (1 - s);
			double q = v * (1 - (s * f));
			double t = v * (1 - (s * (1 - f)));

			switch (i) {
			case 0:
				r = v;
				g = t;
				b = p;

				break;

			case 1:
				r = q;
				g = v;
				b = p;

				break;

			case 2:
				r = p;
				g = v;
				b = t;

				break;

			case 3:
				r = p;
				g = q;
				b = v;

				break;

			case 4:
				r = t;
				g = p;
				b = v;

				break;

			case 5:
				r = v;
				g = p;
				b = q;

				break;
			}
		}

		// now assign everything....
		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
	}


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

	private double[] normalizePoint(double[] point) {
		double[] result = new double[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = (point[i] - this.minima[i]) / (this.maxima[i] - this.minima[i]);
		}
		return result;
	}

	private double[] meanCenterPoint(double[] point) {
		double[] result = new double[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = point[i] - this.empericalMeans[i] + this.meanShift[i];
		}
		return result;
	}

	private double computeMaxDiameter() {
		double diameter = 0;
		for (int i = 0; i < this.maxima.length; i++) {
			diameter = Math.max(diameter, this.maxima[i] - this.minima[i]);
		}
		return diameter;
	}

	private double[] computeMaxima(FiniteMetricSpace<double[]> metricSpace) {
		double[] maxima = new double[dimension];
		for (int j = 0; j < dimension; j++) {
			maxima[j] = Infinity.Double.getNegativeInfinity();
		}
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				maxima[j] = Math.max(maxima[j], metricSpace.getPoint(i)[j]);
			}
		}
		return maxima;
	}

	private double[] computeMinima(FiniteMetricSpace<double[]> metricSpace) {
		double[] minima = new double[dimension];
		for (int j = 0; j < dimension; j++) {
			minima[j] = Infinity.Double.getPositiveInfinity();
		}
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				minima[j] = Math.min(minima[j], metricSpace.getPoint(i)[j]);
			}
		}
		return minima;
	}

	private double[] computeMeans(FiniteMetricSpace<double[]> metricSpace) {
		double[] means = new double[dimension];
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				means[j] += metricSpace.getPoint(i)[j];
			}
		}

		for (int j = 0; j < dimension; j++) {
			means[j] /= numPoints;
		}
		return means;
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
