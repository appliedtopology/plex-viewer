package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;

/**
 * This class contains static functions for drawing various objects. It serves as an API
 * for the plex-viewer project.
 * 
 * @author Andrew Tausz
 *
 */
public class PlexViewer {
	
	public static void main(String[] args) {}
	
	public static void drawSimplexStream(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));
		openGLManager.initialize();
	}
	
	public static void drawSimplexStream(AbstractFilteredStream<Simplex> stream, double[][] points) {
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, points));
		openGLManager.initialize();
	}
	
	public static void drawSimplexStream(GeometricSimplexStream geometricSimplexStream) {
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(geometricSimplexStream));
		openGLManager.initialize();
	}
}
