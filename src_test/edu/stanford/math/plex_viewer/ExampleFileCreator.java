package edu.stanford.math.plex_viewer;

import java.io.IOException;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.io.DoubleArrayReaderWriter;
import edu.stanford.math.plex4.io.SimplexStreamReaderWriter;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.utility.RandomUtility;

public class ExampleFileCreator {
	
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		createRipsExample();
		createWitnessExample();
	}
	
	public static void createRipsExample() {
		int numPoints = 200;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.8;
		double[][] points = PointCloudExamples.getRandomSpherePoints(numPoints, dimension - 1);
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points); 
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension);
		stream.finalizeStream();

		try {
			
			String complexFile = "examples/rips_simplices.txt";
			String pointsFile = "examples/rips_points.txt";
			SimplexStreamReaderWriter.getInstance().writeToFile(stream, complexFile);
			DoubleArrayReaderWriter.getInstance().writeToFile(points, pointsFile);
			
			PlexViewer.renderFromFiles(complexFile, pointsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createWitnessExample() {
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

		try {
			
			String complexFile = "examples/witness_simplices.txt";
			String pointsFile = "examples/witness_points.txt";
			SimplexStreamReaderWriter.getInstance().writeToFile(stream, complexFile);
			DoubleArrayReaderWriter.getInstance().writeToFile(points, pointsFile);
			
			PlexViewer.renderFromFiles(complexFile, pointsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
