package edu.stanford.math.plex_viewer.rendering;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.color.PushforwardColorScheme;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class MappingRenderer implements ObjectRenderer {
	private final SimplexStreamRenderer domainViewer;
	private final SimplexStreamRenderer codomainViewer;
	
	public MappingRenderer(AbstractFilteredStream<Simplex> domainStream, double[][] domainPoints, 
			AbstractFilteredStream<Simplex> codomainStream, double[][] codomainPoints,
			DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> mapping) {
		this.domainViewer = new SimplexStreamRenderer(domainStream, domainPoints);
		this.codomainViewer = new SimplexStreamRenderer(codomainStream, codomainPoints);
		this.codomainViewer.setColorScheme(new PushforwardColorScheme<Simplex, Simplex>(domainViewer.getColorScheme(), mapping));
		
		this.codomainViewer.setMaxDimension(2);
	}
	
	public MappingRenderer(AbstractFilteredStream<Simplex> domainStream, double[][] domainPoints, 
			AbstractFilteredStream<Simplex> codomainStream, double[][] codomainPoints,
			double[][] matrix) {
		this(domainStream, domainPoints, codomainStream, codomainPoints, (new DoubleMatrixConverter<Simplex, Simplex>(domainStream, codomainStream)).toFormalSum(matrix));
	}
	
	public void init(GL gl) {
		this.domainViewer.init(gl);
		this.codomainViewer.init(gl);
	}

	public void processSpecializedKeys(KeyEvent e) {
		this.domainViewer.processSpecializedKeys(e);
		this.codomainViewer.processSpecializedKeys(e);
	}

	public void renderShape(GL gl) {
		this.domainViewer.renderShape(gl);
		this.codomainViewer.renderShape(gl);
	}

}
