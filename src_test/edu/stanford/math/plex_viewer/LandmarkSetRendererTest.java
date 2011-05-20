package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.plex_viewer.gl.OpenGLManager;
import edu.stanford.math.plex_viewer.rendering.LandmarkSetRenderer;

public class LandmarkSetRendererTest {
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testLazyWitnessComplex();
	}
	
	public static void testLazyWitnessComplex() {
		int numPoints = 100;
		int numLandmarkPoints = 10;
		double[][] points = PointCloudExamples.getRandomFigure8Points(numPoints);
		
		LandmarkSelector<double[]> landmark_selector = Plex4.createMaxMinSelector(points, numLandmarkPoints);

		OpenGLManager openGLManager = new OpenGLManager(new LandmarkSetRenderer(landmark_selector));

		openGLManager.initialize();
	}
}
