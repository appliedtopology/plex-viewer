package edu.stanford.math.plex_viewer.pov;

import java.io.IOException;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.io.ObjectWriter;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.color.AveragedSimplicialColorScheme;
import edu.stanford.math.plex_viewer.color.HSBColorScheme;
import edu.stanford.math.plex_viewer.color.PushforwardColorScheme;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleMatrixConverter;

public class MappingPovWriter implements ObjectWriter<MappingDescriptor> {
	SimplexStreamPovWriter domainWriter = new SimplexStreamPovWriter();
	SimplexStreamPovWriter codomainWriter = new SimplexStreamPovWriter();
	
	@Override
	public String getExtension() {
		return "png";
	}

	@Override
	public void writeToFile(MappingDescriptor descriptor, String filepath) throws IOException {
		
		String temp1 = filepath;
		String temp2 = filepath.replaceAll("\\.pov$", "-2.pov");
		
		this.domainWriter.writeToFile(descriptor.domainStream, descriptor.domainPoints, temp1);
		this.domainWriter.writeToFile(descriptor.codomainStream, descriptor.codomainPoints, temp2, new PushforwardColorScheme<Simplex, Simplex>(new AveragedSimplicialColorScheme<double[]>(new GeometricSimplexStream(descriptor.domainStream, descriptor.domainPoints), new HSBColorScheme()), descriptor.mapping));
	}

	public void writeToFile(AbstractFilteredStream<Simplex> domainStream, double[][] domainPoints, 
			AbstractFilteredStream<Simplex> codomainStream, double[][] codomainPoints,
			double[][] matrix, String filepath) throws IOException {		
		
		MappingDescriptor descriptor = new MappingDescriptor();
		
		descriptor.domainStream = domainStream;
		descriptor.domainPoints = domainPoints;
		descriptor.codomainStream = codomainStream;
		descriptor.codomainPoints = codomainPoints;
		descriptor.mapping = (new DoubleMatrixConverter<Simplex, Simplex>(domainStream, codomainStream)).toFormalSum(matrix);
		
		writeToFile(descriptor, filepath);
		
	}
}
