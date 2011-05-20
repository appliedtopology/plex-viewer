package edu.stanford.math.plex_viewer.gl;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex_viewer.color.ColorScheme;

public class GLUtility {
	public static  void drawPoint(GL gl, double[] point, float[] color) {
		gl.glColor3fv(color, 0);

		if (point.length == 1) {
			gl.glVertex2d(point[0], 0);
		} else if (point.length == 2) {
			gl.glVertex2d(point[0], point[1]);
		} else if (point.length >= 3) {
			gl.glVertex3d(point[0], point[1], point[2]);
		}
	}
	
	public static  void drawPoint(GL gl, double[] point, ColorScheme<double[]> colorScheme) {
		drawPoint(gl, point, colorScheme.computeColor(point));
	}
	
	public static void drawSimplex(GL gl, Simplex simplex, ColorScheme<Simplex> colorScheme, AbstractObjectMetricSpace<double[]> metricSpace) {
		drawSimplex(gl, simplex, colorScheme.computeColor(simplex), metricSpace);
	}
	
	public static void drawSimplex(GL gl, Simplex simplex, float[] color, AbstractObjectMetricSpace<double[]> metricSpace) {
		
		int glShapeCode = 0;
		if (simplex.getDimension() == 0) {
			glShapeCode = GL.GL_POINTS;
		} else if (simplex.getDimension() == 1) {
			glShapeCode = GL.GL_LINES;
		} else if (simplex.getDimension() == 2) {
			glShapeCode = GL.GL_TRIANGLES;
		}
		
		int[] vertices = simplex.getVertices();
		gl.glBegin(glShapeCode);
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] point = metricSpace.getPoint(vertices[vertexIndex]);
			gl.glColor3fv(color, 0);
			if (point.length == 1) {
				gl.glVertex2d(point[0], 0);
			} else if (point.length == 2) {
				gl.glVertex2d(point[0], point[1]);
			} else if (point.length >= 3) {
				gl.glVertex3d(point[0], point[1], point[2]);
			}
		}
		gl.glEnd();
	}
}
