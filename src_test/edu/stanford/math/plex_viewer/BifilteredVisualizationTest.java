package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.multi.AbstractMultifilteredStream;
import edu.stanford.math.plex4.streams.multi.BifilteredMetricStream;
import edu.stanford.math.plex4.streams.multi.FilterFunction;
import edu.stanford.math.plex4.streams.multi.IncreasingOrthantFlattener;
import edu.stanford.math.plex4.streams.multi.InducedSimplicialFilterFunction;
import edu.stanford.math.plex4.streams.multi.IntFilterFunction;
import edu.stanford.math.plex4.streams.multi.KernelDensityFilterFunction;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.plex_viewer.gl.OpenGLManager;
import edu.stanford.math.plex_viewer.rendering.SimplexStreamRenderer;

public class BifilteredVisualizationTest {
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testMultifilteredFlattening();
	}

	public static void testMultifilteredFlattening() {
		// initialize constants
		int n = 100;
		int maxDimension = 1;
		double maxFiltrationValue = 0.4;
		double sigma = 0.4;

		// set direction of sets
		double[] principalDirection = new double[] { 0.01, 0.01 };

		// create a new metric space from random points on a circle
		double[][] points = PointCloudExamples.getRandomCirclePoints(n);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);

		// create a vietoris rips complex from the points
		VietorisRipsStream<double[]> stream = Plex4.createVietorisRipsStream(
				metricSpace, maxDimension + 1, maxFiltrationValue);
		stream.finalizeStream();

		// initialize the kernel density function
		IntFilterFunction intFilterFunction = new KernelDensityFilterFunction(
				metricSpace, sigma);
		FilterFunction<Simplex> simplexFilterFunction = new InducedSimplicialFilterFunction(
				intFilterFunction);

		// create the bifiltered stream
		AbstractMultifilteredStream<Simplex> multifilteredStream = new BifilteredMetricStream<Simplex>(
				stream, simplexFilterFunction);

		// create a "flattened" version of the stream by considering increasing
		// subsets
		IncreasingOrthantFlattener<Simplex> flattener = new IncreasingOrthantFlattener<Simplex>(
				principalDirection);
		AbstractFilteredStream<Simplex> flattenedStream = flattener
				.collapse(multifilteredStream);
		
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamRenderer(flattenedStream, metricSpace));

		openGLManager.initialize();
	}
}
