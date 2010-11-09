package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;

public class MappingViewerTest {
	
	public static void main(String[] args) {
		RandomUtility.initializeWithSeed(0);
		testMappingViewer();
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
}
