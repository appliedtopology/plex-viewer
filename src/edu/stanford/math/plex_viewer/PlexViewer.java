package edu.stanford.math.plex_viewer;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;

public class PlexViewer {
	public static void drawSimplexStream(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, metricSpace));
		openGLManager.initialize();
	}
	
	public static void drawSimplexStream(AbstractFilteredStream<Simplex> stream, double[][] points) {
		OpenGLManager openGLManager = new OpenGLManager(new SimplexStreamViewer(stream, points));
		openGLManager.initialize();
	}
}
