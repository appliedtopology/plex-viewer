package edu.stanford.math.plex_viewer.rendering;

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.gl.GLSettings;
import edu.stanford.math.plex_viewer.gl.GLUtility;

public class LandmarkStreamRenderer extends SimplexStreamRenderer {

	protected final LandmarkSelector<double[]> landmarkSelector;
	protected final Set<Integer> landmarkSet = new HashSet<Integer>();
	
	public LandmarkStreamRenderer(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace, LandmarkSelector<double[]> landmarkSelector) {
		super(stream, metricSpace);
		this.landmarkSelector = landmarkSelector;
		int[] selection = landmarkSelector.getLandmarkPoints();
		for (int i: selection) {
			this.landmarkSet.add(i);
		}
	}

	public LandmarkStreamRenderer(AbstractFilteredStream<Simplex> stream, double[][] points, LandmarkSelector<double[]> landmarkSelector) {
		super(stream, points);
		this.landmarkSelector = landmarkSelector;
		int[] selection = landmarkSelector.getLandmarkPoints();
		for (int i: selection) {
			this.landmarkSet.add(i);
		}
	}

	public LandmarkStreamRenderer(GeometricSimplexStream geometricSimplexStream, LandmarkSelector<double[]> landmarkSelector) {
		super(geometricSimplexStream);
		this.landmarkSelector = landmarkSelector;
		int[] selection = landmarkSelector.getLandmarkPoints();
		for (int i: selection) {
			this.landmarkSet.add(i);
		}
	}

	public void renderShape(GL gl) {
		double[][] allPoints = this.landmarkSelector.getUnderlyingMetricSpace().getPoints();
		
		gl.glBegin(GL.GL_POINTS);
		for (int i = 0; i < allPoints.length; i++) {
			if (!this.landmarkSet.contains(i)) {
				GLUtility.drawPoint(gl, allPoints[i], GLSettings.defaultPointColor);
			}
		}
		gl.glEnd();
		
		
		super.renderShape(gl);
	}
}
