package edu.stanford.math.plex_viewer.rendering;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_viewer.color.ColorScheme;
import edu.stanford.math.plex_viewer.color.HSBColorScheme;
import edu.stanford.math.plex_viewer.gl.GLSettings;
import edu.stanford.math.plex_viewer.gl.GLUtility;

public class LandmarkSetRenderer implements ObjectRenderer {

	private final double[][] points;
	private final Set<Integer> landmarkSet = new HashSet<Integer>();
	private ColorScheme<double[]> colorScheme = new HSBColorScheme();

	public LandmarkSetRenderer(double[][] points, int[] selection) {
		this.points = points;
		for (int i: selection) {
			this.landmarkSet.add(i);
		}
	}

	public LandmarkSetRenderer(LandmarkSelector<double[]> selector) {
		this.points = selector.getUnderlyingMetricSpace().getPoints();
		int[] selection = selector.getLandmarkPoints();
		for (int i: selection) {
			this.landmarkSet.add(i);
		}
	}

	@Override
	public void init(GL gl) {
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_POINT_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glPointSize(GLSettings.defaultPointSize);
	}

	@Override
	public void processSpecializedKeys(KeyEvent e) {}

	@Override
	public void renderShape(GL gl) {
		gl.glBegin(GL.GL_POINTS);
		for (int i = 0; i < this.points.length; i++) {	
			if (!this.landmarkSet.contains(i)) {
				GLUtility.drawPoint(gl, points[i], colorScheme);
			}
		}
		
		for (int i = 0; i < this.points.length; i++) {	
			if (this.landmarkSet.contains(i)) {
				GLUtility.drawPoint(gl, points[i], GLSettings.defaultPointColor);
			}
		}
		gl.glEnd();
	}
}
