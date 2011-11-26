package edu.stanford.math.plex_viewer.pov;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class MappingDescriptor {
	
	public AbstractFilteredStream<Simplex> domainStream;
	public double[][] domainPoints; 
	public AbstractFilteredStream<Simplex> codomainStream;
	public double[][] codomainPoints;
	public DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> mapping;
	
}
