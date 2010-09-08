package main_package;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_viewer.ObjectRenderer;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;

public class GraphViewer implements ObjectRenderer {
	protected AbstractUndirectedGraph graph;
	protected EuclideanMetricSpace metricSpace;
	
	public GraphViewer(AbstractUndirectedGraph graph, EuclideanMetricSpace metricSpace) {
		this.graph = graph;
		this.metricSpace = metricSpace;
	}
	
	public void renderShape(GL gl) {
		int n = this.graph.getNumVertices();
		
		for (int i = 0; i < n; i++) {
			TIntSet set = graph.getLowerNeighbors(i);
			for (TIntIterator iterator = set.iterator(); iterator.hasNext(); ) {
				int j = iterator.next();
				double[] point_i = this.metricSpace.getPoint(i);
				double[] point_j = this.metricSpace.getPoint(j);
				
				float g = ((float) i) / ((float) n);
				float b = ((float) j) / ((float) n);
				
				gl.glBegin(GL.GL_LINES);
				gl.glColor3d(0.2f, point_i[0], point_i[1]); 
				gl.glVertex3d(point_i[0], point_i[1], 0);
				gl.glColor3d(0.2f, point_j[0], point_j[1]); 
				gl.glVertex3d(point_j[0], point_j[1], 0);
				gl.glEnd();
				
			}
		}
	}

	public void processSpecializedKeys(KeyEvent e) {}

	public void init(GL gl) {
		// TODO Auto-generated method stub
	}
}
