package main_package;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;

import edu.stanford.math.plex4.algebraic_structures.impl.DoubleArrayModule;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex4.array_utility.ArrayConversion;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.free_module.AbstractGenericFreeModule;
import edu.stanford.math.plex4.free_module.DoubleFormalSum;
import edu.stanford.math.plex4.free_module.ModuleMorphismRepresentation;
import edu.stanford.math.plex4.free_module.UnorderedGenericFreeModule;
import edu.stanford.math.plex4.functional.GenericDoubleFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.mapping.HomComplexComputation;
import edu.stanford.math.plex4.homology.mapping.MappingUtility;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.io2.FileIOUtility;
import edu.stanford.math.plex4.math.metric.interfaces.FiniteMetricSpace;
import gnu.trove.TObjectDoubleIterator;

public class MappingViewer<F extends Number> implements ObjectRenderer {
	private final AbstractFilteredStream<Simplex> domainStream;
	private final AbstractFilteredStream<Simplex> codomainStream;
	
	private final SimplexStreamViewer domainViewer;
	private final SimplexStreamViewer codomainViewer;
	private final GenericOrderedField<F> field;
	private final AbstractGenericFreeModule<F, GenericPair<Simplex, Simplex>> chainModule;
	
	public MappingViewer(AbstractFilteredStream<Simplex> domainStream, FiniteMetricSpace<double[]> domainMetricSpace,
			AbstractFilteredStream<Simplex> codomainStream, FiniteMetricSpace<double[]> codomainMetricSpace,
			GenericOrderedField<F> field) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		this.domainStream = domainStream;
		this.codomainStream = codomainStream;
		this.domainViewer = new SimplexStreamViewer(domainStream, domainMetricSpace);
		this.codomainViewer = new SimplexStreamViewer(codomainStream, codomainMetricSpace);
		this.field = field;
		this.chainModule = new UnorderedGenericFreeModule<F, GenericPair<Simplex, Simplex>>(this.field);
		
		
		this.domainViewer.setMeanShift(new double[]{-2, 0});
		this.codomainViewer.setMeanShift(new double[]{2, 0});
		
		/*
		GenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle = this.computeMapping();
		GenericFunction<Simplex, GenericFormalSum<F, Simplex>> mapping = MappingUtility.toFunctionObject(generatingCycle);
		
		this.codomainViewer.setColorFunction(this.pushforwardColorFunction(mapping, this.domainViewer.getDefaultColorFunction(), this.domainStream));
		*/
		
		DoubleFormalSum<GenericPair<Simplex, Simplex>> optimalChainMap = this.computeFunction();
		
		//this.codomainViewer.setColorFunction(this.pushforwardColorFunction(MappingUtility.toFunctionObject(optimalChainMap), this.domainViewer.getDefaultColorFunction(), this.domainStream));
		double c = (double) codomainMetricSpace.size() / (double) domainMetricSpace.size();
		c = 1;
		this.codomainViewer.setColorFunction(MappingUtility.pushforward(MappingUtility.toFunctionObject(scalarMultiply(abs(optimalChainMap), c)), this.domainViewer.getDefaultColorFunction(), this.domainStream, new DoubleArrayModule(3)));
	}
	
	private DoubleFormalSum<GenericPair<Simplex, Simplex>> computeFunction() {
		ModuleMorphismRepresentation<F, Simplex, Simplex> rep = new ModuleMorphismRepresentation<F, Simplex, Simplex>(this.domainStream, this.codomainStream);
		List<double[]> list = null;
		try {
			list = FileIOUtility.readNumericCSVFile("../javaplex/src/matlab/mapping/corner_point.txt", ",");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[][] matrix = ArrayConversion.toMatrix(list);
		return rep.toBasisMapping(matrix);		
	}
	
	
	private DoubleFormalSum<GenericPair<Simplex, Simplex>> computeOptimalFunction() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		HomComplexComputation<F> computation = new HomComplexComputation<F>(domainStream, codomainStream, SimplexComparator.getInstance(), SimplexComparator.getInstance(), field);
		
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = computation.computeGeneratingCycles();
		AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> cycleSum = computation.sumGeneratingCycles(generatingCycles);
		
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies = computation.getChainHomotopies();
		
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			//System.out.println(homotopy);
		}
		
		//GenericDoubleFunction<DoubleFormalSum<Simplex>> imagePenaltyFunction = MappingUtility.getNormFunction(1);
		//GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.computeInducedFunction(imagePenaltyFunction, domainStream);
		
		GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.getSimplicialityLossFunction(domainStream, codomainStream);
		
		DoubleFormalSum<GenericPair<Simplex, Simplex>> optimalChainMap = computation.findOptimalChainMap(cycleSum, homotopies, mappingPenaltyFunction);
		//optimalChainMap = MappingUtility.roundCoefficients(optimalChainMap);
		
		//System.out.println(cycleSum);
		System.out.println(optimalChainMap);
		
		//return MappingUtility.toDoubleFormalSum(cycleSum);
		return optimalChainMap;
	}
	
	private DoubleFormalSum<GenericPair<Simplex, Simplex>> abs(DoubleFormalSum<GenericPair<Simplex, Simplex>> chainMap)
	{
		DoubleFormalSum<GenericPair<Simplex, Simplex>> result = new DoubleFormalSum<GenericPair<Simplex, Simplex>>();
		for (TObjectDoubleIterator<GenericPair<Simplex, Simplex>> iterator = chainMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.put(Math.abs(iterator.value()), iterator.key());
		}
		return result;
	}
	
	private DoubleFormalSum<GenericPair<Simplex, Simplex>> scalarMultiply(DoubleFormalSum<GenericPair<Simplex, Simplex>> chainMap, double c)
	{
		DoubleFormalSum<GenericPair<Simplex, Simplex>> result = new DoubleFormalSum<GenericPair<Simplex, Simplex>>();
		for (TObjectDoubleIterator<GenericPair<Simplex, Simplex>> iterator = chainMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.put(c * (iterator.value()), iterator.key());
		}
		return result;
	}
	
	
	public void processSpecializedKeys(KeyEvent e) {
		this.domainViewer.processSpecializedKeys(e);
		this.codomainViewer.processSpecializedKeys(e);
	}

	public void renderShape(GL gl) {
		this.domainViewer.renderShape(gl);
		this.codomainViewer.renderShape(gl);
	}

	public void init(GL gl) {
		this.domainViewer.init(gl);
		this.codomainViewer.init(gl);
	}
}
