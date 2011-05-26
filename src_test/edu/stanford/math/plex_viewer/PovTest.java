package edu.stanford.math.plex_viewer;

import java.io.IOException;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.plex_viewer.pov.LandmarkSelectorPovWriter;
import edu.stanford.math.plex_viewer.pov.SimplexStreamPovWriter;

public class PovTest {
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testVietorisRipsComplex();
	}

	public static void testVietorisRipsComplex() {
		int numPoints = 600;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.2;
		double[][] points = PointCloudExamples.getRandomSpherePoints(numPoints, dimension - 1);
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension);
		stream.finalizeStream();

		SimplexStreamPovWriter w = new SimplexStreamPovWriter();

		try {
			w.writeToFile(stream, metricSpace, "test.pov");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testLazyWitnessComplex() {
		int numPoints = 2000;
		int numLandmarkPoints = 100;
		int maxDimension = 2;
		double maxFiltrationValue = 0.0;
		double[][] points = PointCloudExamples.getRandomTorusPoints(numPoints, 0.5, 1);
		
		LandmarkSelector<double[]> landmark_selector = Plex4.createMaxMinSelector(points, numLandmarkPoints);
		
		LazyWitnessStream<double[]> stream = Plex4.createLazyWitnessStream(landmark_selector, maxDimension, maxFiltrationValue);
		stream.finalizeStream();
	
		LandmarkSelectorPovWriter w = new LandmarkSelectorPovWriter();
		
		try {
			w.writeToFile(landmark_selector , "test.pov");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
