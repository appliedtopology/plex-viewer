package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.api.FilteredStreamInterface;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;

public class SimplexViewerTest {

	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testVietorisRipsComplex();
	}

	public static void testVietorisRipsComplex() {
		int numPoints = 200;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.8;
		double[][] points = PointCloudExamples.getGaussianPoints(numPoints, dimension);
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension);
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));

		openGLManager.initialize();
	}

	public static void testFigure8() {
		int numPoints = 200;
		int numLandmarkPoints = 50;
		int maxDimension = 2;
		double maxFiltrationValue = 0.2;
		int numDivisions = 10;

		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomFigure8Points(numPoints)); 
		LandmarkSelector<double[]> selector = new MaxMinLandmarkSelector<double[]>(metricSpace, numLandmarkPoints);
		AbstractFilteredStream<Simplex> stream = FilteredStreamInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, selector));

		openGLManager.initialize();

	}
}
