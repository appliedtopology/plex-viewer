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
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex_viewer.ObjectRenderer;
import edu.stanford.math.plex_viewer.OpenGLManager;

public class OpenGLTest {
	public static void main(String[] args) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		testSphere();		
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
		int domainCardinality = 4;
		int codomainCardinality = 6;
		int d = 2;
		
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domainCardinality);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		//AbstractFilteredStream<Simplex> domainStream = SimplicialComplexOperations.disjointUnion(SimplexStreamExamples.getCircle(3), SimplexStreamExamples.getCircle(3));
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getTorus();
		
		ObjectRenderer renderer = new MappingViewer<Fraction>(domainStream, domainMetricSpace, codomainStream, codomainMetricSpace, RationalField.getInstance());
		OpenGLManager openGLManager = new OpenGLManager(renderer);		
		openGLManager.initialize();
	}
	
	public static void testMapping() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int domainCardinality = 20;
		int codomainCardinality = 20;
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
