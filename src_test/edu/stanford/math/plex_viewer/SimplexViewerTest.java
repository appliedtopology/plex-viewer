package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.api.FilteredStreamInterface;
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;

public class SimplexViewerTest {

	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testLazyWitnessComplex();
	}

	public static void testVietorisRipsComplex() {
		int numPoints = 200;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.8;
		double[][] points = PointCloudExamples.getRandomSpherePoints(numPoints, dimension - 1);
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension);
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));

		openGLManager.initialize();
	}
	
	public static void testLazyWitnessComplex() {
		int numPoints = 200;
		int numLandmarkPoints = 40;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.5;
		double[][] points = PointCloudExamples.getRandomTrefoilKnotPoints(numPoints);
		
		LandmarkSelector<double[]> landmark_selector = Plex4.createMaxMinSelector(points, numLandmarkPoints);
		
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		LazyWitnessStream<double[]> stream = Plex4.createLazyWitnessStream(landmark_selector, maxDimension, maxFiltrationValue);
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, landmark_selector));

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
	
	public static void testIcosahedron() {
		
		double[][] points = PointCloudExamples.getIcosahedronVertices();
		for (int i = 0; i < points.length; i++) {
			points[i][0] -= 0.2;
		}
		
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getIcosahedron();
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));

		openGLManager.initialize();
	}
	
	public static void testAnnulus() {
		int width = 4;
		int length = 20;
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getAnnulusVertices(width, length)); 
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getAnnulus(width, length);
		stream.finalizeStream();

		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));

		openGLManager.initialize();
	}
}
