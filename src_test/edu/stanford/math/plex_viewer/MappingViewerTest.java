package edu.stanford.math.plex_viewer;

import java.io.IOException;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.io.DoubleArrayReaderWriter;
import edu.stanford.math.plex4.io.SimplexStreamReaderWriter;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

public class MappingViewerTest {
	
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testFileMapping();
	}
	
	public static void testMappingViewer() {
		int domain_size = 4;
		int codomain_size = 10;
		
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
	
	public static void testFileMapping() {
		String domainPointsFile = "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\domain_points.txt";
		String codomainPointsFile = "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\codomain_points.txt";
		String domainStreamFile = "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\domain_stream.txt";
		String codomainStreamFile = "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\codomain_stream.txt";
		String mappingFile = "D:\\Documents\\Code\\javaplex\\src\\matlab\\hom_complex\\mapping.txt";
		
		try {
			double[][] domainPoints = DoubleArrayReaderWriter.getInstance().importFromFile(domainPointsFile);
			double[][] codomainPoints = DoubleArrayReaderWriter.getInstance().importFromFile(codomainPointsFile);
			AbstractFilteredStream<Simplex> domainStream = SimplexStreamReaderWriter.getInstance().importFromFile(domainStreamFile);
			AbstractFilteredStream<Simplex> codomainStream = SimplexStreamReaderWriter.getInstance().importFromFile(codomainStreamFile);
			double[][] mapping = DoubleArrayReaderWriter.getInstance().importFromFile(mappingFile);
			
			for (int i = 0; i < domainPoints.length; i++) {
				domainPoints[i][0] += 8;
			}
			
			//PlexViewer.drawMapping(domainStream, domainPoints, codomainStream, codomainPoints, mapping);
			PlexViewer.drawMapping(codomainStream, codomainPoints, domainStream, domainPoints, DoubleArrayMath.transpose(mapping));
			//PlexViewer.drawSimplexStream(domainStream, domainPoints);
			//PlexViewer.drawSimplexStream(codomainStream, codomainPoints);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public static void testFileMapping() {
		int domain_size = 4;
		int codomain_size = 10;
		
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
		
		double[][] mapping = csvToMatrix(filename);
		PlexViewer.drawMapping(domainStream, domainPoints, codomainStream, codomainPoints, mapping);
	}
	
	public static void testMappingViewer() {
		int domain_size = 4;
		int codomain_size = 10;
		
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
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] += 5;
		}
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] -= 0;
		}
		
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
		
		for (int i = 0; i < codomainPoints.length; i++) {
			codomainPoints[i][0] += 3;
		}
		
		for (int i = 0; i < domainPoints.length; i++) {
			domainPoints[i][0] -= 0.2;
		}
		
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
		int domain_size = 4;
		int codomain_size = 10;
		
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
		
		double[][] mapping = csvToMatrix(filename);
		PlexViewer.drawMapping(domainStream, domainPoints, codomainStream, codomainPoints, mapping);
	}
	
	public static double[][] csvToMatrix(String filename) {
		double[][] mapping = null;
		try {
			List<double[]> rows = FileIOUtility.readNumericCSVFile(filename, ",");
			int n = rows.size();
			 mapping = new double[n][];
			for (int i = 0; i < n; i++) {
				mapping[i] = rows.get(i);
			}
			
			return mapping;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	*/
}
