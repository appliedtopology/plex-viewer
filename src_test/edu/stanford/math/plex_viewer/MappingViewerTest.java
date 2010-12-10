package edu.stanford.math.plex_viewer;

import java.io.IOException;
import java.util.List;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.io.FileIOUtility;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;

public class MappingViewerTest {
	
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testWitnessMapping();
	}
	
	public static void testMappingViewer() {
		int domain_size = 4;
		int codomain_size = 3;
		
		double[][] domainPoints = PointCloudExamples.getEquispacedCirclePoints(domain_size);
		double[][] codomainPoints = PointCloudExamples.getEquispacedCirclePoints(codomain_size);
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domain_size);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomain_size);
		
		double[][] mapping = new double[codomain_size * 2][domain_size * 2];
		mapping[0][0] = 1;
		mapping[1][1] = 1;
		mapping[2][2] = 1;
		
		PlexViewer.drawMapping(domainStream, domainPoints, codomainStream, codomainPoints, mapping);
		
	}
	
	public static void testAnnulus() {
		int width = 4;
		int length = 8;
		int codomain_size = 4;
		
		double[][] domainPoints = PointCloudExamples.getAnnulusVertices(width, length);
		double[][] codomainPoints = PointCloudExamples.getEquispacedCirclePoints(codomain_size);
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] += 3;
		}
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] -= 0.2;
		}
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getAnnulus(width, length);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomain_size);

		//viewFileMap(domainStream, codomainStream, domainPoints, codomainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
		viewFileMap(codomainStream, domainStream, codomainPoints, domainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
	}
	
	
	public static void testTetraOcta() {
		
		double[][] domainPoints = PointCloudExamples.getOctahedronVertices();
		double[][] codomainPoints = PointCloudExamples.getIcosahedronVertices();
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] += 3;
		}
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] -= 0.2;
		}
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getOctahedron();
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getIcosahedron();

		//viewFileMap(domainStream, codomainStream, domainPoints, codomainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
		viewFileMap(codomainStream, domainStream, codomainPoints, domainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
	}
	
	public static void testWitnessMapping2() {		
		int numPoints = 200;
		int numLandmarkPoints = 40;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.5;
		
		int domain_size = 4;
		int codomain_size = numPoints;
		
		double[][] domainPoints = PointCloudExamples.getEquispacedCirclePoints(domain_size);
		double[][] codomainPoints = PointCloudExamples.getRandomTrefoilKnotPoints(codomain_size);
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] += 3;
		}
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] -= 0.2;
		}
		/*
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] += 5;
		}
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] -= 0;
		}*/
		
		LandmarkSelector<double[]> landmark_selector = Plex4.createMaxMinSelector(codomainPoints, numLandmarkPoints);
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domain_size);
		AbstractFilteredStream<Simplex> codomainStream = Plex4.createLazyWitnessStream(landmark_selector, maxDimension, maxFiltrationValue);

		viewFileMap(domainStream, codomainStream, domainPoints, landmark_selector.getPoints(), "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
		//viewFileMap(codomainStream, domainStream, landmark_selector.getPoints(), domainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
	}
	
	public static void testWitnessMapping() {		
		int numPoints = 200;
		int numLandmarkPoints = 40;
		int dimension = 3;
		int maxDimension = 2;
		double maxFiltrationValue = 0.5;
		
		int domain_size = 4;
		int codomain_size = numPoints;
		
		double[][] domainPoints = PointCloudExamples.getEquispacedCirclePoints(domain_size);
		double[][] codomainPoints = PointCloudExamples.getRandomTrefoilKnotPoints(codomain_size);
		/*
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] += 3;
		}
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] -= 0.2;
		}*/
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] += 5;
		}
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] -= 0;
		}
		
		LandmarkSelector<double[]> landmark_selector = Plex4.createMaxMinSelector(codomainPoints, numLandmarkPoints);
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domain_size);
		AbstractFilteredStream<Simplex> codomainStream = Plex4.createLazyWitnessStream(landmark_selector, maxDimension, maxFiltrationValue);

		//viewFileMap(domainStream, codomainStream, domainPoints, landmark_selector.getPoints(), "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
		viewFileMap(codomainStream, domainStream, landmark_selector.getPoints(), domainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
	}
	
	
	public static void testFileMapping() {
		int domain_size = 5;
		int codomain_size = 40;
		
		double[][] domainPoints = PointCloudExamples.getEquispacedCirclePoints(domain_size);
		double[][] codomainPoints = PointCloudExamples.getEquispacedCirclePoints(codomain_size);
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] += 3;
		}
		
		
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domain_size);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomain_size);

		viewFileMap(domainStream, codomainStream, domainPoints, codomainPoints, "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\matrix.txt");
	}
	
	public static void viewFileMap(AbstractFilteredStream<Simplex> domainStream, AbstractFilteredStream<Simplex> codomainStream, 
			double[][] domainPoints, double[][] codomainPoints,	String filename) {
		
		try {
			List<double[]> rows = FileIOUtility.readNumericCSVFile(filename, ",");
			int n = rows.size();
			double[][] mapping = new double[n][];
			for (int i = 0; i < n; i++) {
				mapping[i] = rows.get(i);
			}
			
			PlexViewer.drawMapping(domainStream, domainPoints, codomainStream, codomainPoints, mapping);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
