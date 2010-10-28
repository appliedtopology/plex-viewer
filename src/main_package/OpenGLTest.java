package main_package;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.optimization.OptimizationException;

import edu.stanford.math.plex4.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex4.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex4.deprecated_tests.PersistentHomologyTest;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.RandomLandmarkSelector;

public class OpenGLTest {
	public static void main(String[] args) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		testCircleMapping();		
	}
	
	public static double[][] getoctahedronVertices() {
		double[][] points = new double[6][3];
		
		points[0] = new double[]{1, 0, 0};
		points[1] = new double[]{0, -1, 0};
		points[2] = new double[]{-1, 0, 0};
		points[3] = new double[]{0, 1, 0};
		points[4] = new double[]{0, 0, 1};
		points[5] = new double[]{0, 0, -1};
		
		return points;
	}
	
	public static ExplicitStream<Simplex> getOctahedron() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[] {0}), 0);
		stream.addElement(new Simplex(new int[] {1}), 0);
		stream.addElement(new Simplex(new int[] {2}), 0);
		stream.addElement(new Simplex(new int[] {3}), 0);
		stream.addElement(new Simplex(new int[] {4}), 0);
		stream.addElement(new Simplex(new int[] {5}), 0);

		stream.addElement(new Simplex(new int[] {0, 1}), 0);
		stream.addElement(new Simplex(new int[] {0, 3}), 0);
		stream.addElement(new Simplex(new int[] {0, 4}), 0);
		stream.addElement(new Simplex(new int[] {0, 5}), 0);
		
		stream.addElement(new Simplex(new int[] {1, 2}), 0);
		stream.addElement(new Simplex(new int[] {1, 4}), 0);
		stream.addElement(new Simplex(new int[] {1, 5}), 0);
		
		stream.addElement(new Simplex(new int[] {2, 3}), 0);
		stream.addElement(new Simplex(new int[] {2, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 5}), 0);

		stream.addElement(new Simplex(new int[] {3, 4}), 0);
		stream.addElement(new Simplex(new int[] {3, 5}), 0);

		stream.addElement(new Simplex(new int[] {0, 1, 4}), 0);
		stream.addElement(new Simplex(new int[] {1, 2, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 3, 4}), 0);
		stream.addElement(new Simplex(new int[] {0, 3, 4}), 0);
		
		stream.addElement(new Simplex(new int[] {0, 1, 5}), 0);
		stream.addElement(new Simplex(new int[] {1, 2, 5}), 0);
		stream.addElement(new Simplex(new int[] {2, 3, 5}), 0);
		stream.addElement(new Simplex(new int[] {0, 3, 5}), 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static double[][] gettetrahedronVertices() {
		double[][] points = new double[4][3];
		
		points[0] = new double[]{0, 0, 0};
		points[1] = new double[]{1, 0, 0};
		points[2] = new double[]{0, 1, 0};
		points[3] = new double[]{0, 0, 1};
		
		return points;
	}
	
	public static void testSphere() {
		int n = 1000;
		int d = 2;
		SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, d));
		//SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(n));
		
		LandmarkSelector<double[]> selector = new RandomLandmarkSelector<double[]>(metricSpace, 100);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, 3, 0.5, 10);
		stream.finalizeStream();
		
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, selector));
		
		openGLManager.initialize();
		
		PersistentHomologyTest.testClassicalPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2), 3);
	}
	
	public static void testCircleMapping() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int domainCardinality = 3;
		int codomainCardinality = 3;
		int d = 2;
		
		
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(gettetrahedronVertices());
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(getoctahedronVertices());
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getTetrahedron();
		AbstractFilteredStream<Simplex> codomainStream = getOctahedron();
		
		//SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		//SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		//AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domainCardinality);
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		
		/*
		RandomUtility.initializeWithSeed(0);
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(40, 1));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(40, 1));
		int n = 100;
		int l = 30;
		double r_max = 0.1;
		LandmarkSelector<double[]> domainSelector = new MaxMinLandmarkSelector<double[]>(domainMetricSpace, l);
		LandmarkSelector<double[]> codomainSelector = new MaxMinLandmarkSelector<double[]>(domainMetricSpace, l);
		AbstractFilteredStream<Simplex> domainStream = new LazyWitnessStream<double[]>(domainMetricSpace, domainSelector, 1, r_max, 20);
		AbstractFilteredStream<Simplex> codomainStream = new LazyWitnessStream<double[]>(codomainMetricSpace, codomainSelector, 1, r_max, 20);
		
		domainStream.finalizeStream();
		codomainStream.finalizeStream();
		*/
		//AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domainCardinality);
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		//AbstractFilteredStream<Simplex> domainStream = SimplicialComplexOperations.disjointUnion(SimplexStreamExamples.getCircle(3), SimplexStreamExamples.getCircle(3));
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getTorus();
		
		ObjectRenderer renderer = new MappingViewer<Fraction>(domainStream, domainMetricSpace, codomainStream, codomainMetricSpace, RationalField.getInstance());
		OpenGLManager openGLManager = new OpenGLManager(renderer);		
		openGLManager.initialize();
	}
	
	public static void testMapping() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int domainCardinality = 4;
		int codomainCardinality = 4;
		int d = 3;
		
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(domainCardinality, d));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(codomainCardinality, d));
		
		LandmarkSelector<double[]> domainSelector = new MaxMinLandmarkSelector<double[]>(domainMetricSpace, domainCardinality);
		LazyWitnessStream<double[]> domainStream = new LazyWitnessStream<double[]>(domainMetricSpace, domainSelector, 3, 0.5, 10);
		domainStream.finalizeStream();
		
		LandmarkSelector<double[]> codomainSelector = new MaxMinLandmarkSelector<double[]>(codomainMetricSpace, codomainCardinality);
		LazyWitnessStream<double[]> codomainStream = new LazyWitnessStream<double[]>(codomainMetricSpace, codomainSelector, 3, 0.5, 10);
		codomainStream.finalizeStream();
		
		ObjectRenderer renderer = new MappingViewer<Fraction>(domainStream, domainSelector, codomainStream, codomainSelector, RationalField.getInstance());
		OpenGLManager openGLManager = new OpenGLManager(renderer);		
		openGLManager.initialize();
	}
	
	public static void testMappingTriangle() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int domainCardinality = 3;
		int codomainCardinality = 3;
		int d = 3;
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domainCardinality);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		
		SearchableFiniteMetricSpace<double[]> domainSelector = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		SearchableFiniteMetricSpace<double[]> codomainSelector = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		
		ObjectRenderer renderer = new MappingViewer<Fraction>(domainStream, domainSelector, codomainStream, codomainSelector, RationalField.getInstance());
		OpenGLManager openGLManager = new OpenGLManager(renderer);		
		openGLManager.initialize();
	}
}
