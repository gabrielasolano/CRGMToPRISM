package br.unb.cic.rtgoretoprism.paramwrapper;

import br.unb.cic.rtgoretoprism.generator.CodeGenerationException;

public interface ParametricModelChecker {
	/**
	 * Evaluates the (parametric) reliability of an FDTMC.
	 * By convention, we assume that each success state is labeled
	 * with the string "success".
	 *
	 * @param fdtmc FDTMC to be evaluated.
	 * @return TODO
	 * @return Formula parameterized on the transition probabilities.
	 * @throws CodeGenerationException 
	 */
	public String getFormula(String model) throws CodeGenerationException;
}
