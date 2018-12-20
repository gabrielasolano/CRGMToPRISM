/**
 * <copyright>
 *
 * TAOM4E - Tool for Agent Oriented Modeling for the Eclipse Platform
 * Copyright (C) ITC-IRST, Trento, Italy
 * Author: Mirko Morandini
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * The electronic copy of the license can be found here:
 * http://sra.itc.it/tools/taom/freesoftware/gpl.txt
 *
 * The contact information:
 * e-mail: taom4e@itc.it
 * site: http://sra.itc.it/tools/taom4e/
 *
 * </copyright>
 */

package br.unb.cic.rtgoretoprism.generator.goda.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import br.unb.cic.rtgoretoprism.console.ATCConsole;
import br.unb.cic.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.rtgoretoprism.generator.goda.parser.CtxParser;
import br.unb.cic.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.rtgoretoprism.model.ctx.CtxSymbols;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.rtgoretoprism.paramformula.GenerateCombination;
import br.unb.cic.rtgoretoprism.util.PathLocation;
import it.itc.sra.taom4e.model.core.informalcore.Plan;

/**
 * Writes the agent from the internal representation
 * 
 * @author Mirko Morandini
 * @author bertolini (comments, reorganizetion)
 */
public class PrismWriter {
	/** the set of placeholder founded into template files that are 
	 * substituted with the proper values during the code generation
	 * process. */

	private static final String MODULE_NAME_TAG			= "$MODULE_NAME$";
	private static final String TIME_SLOT_TAG			= "$TIME_SLOT";
	private static final String PREV_TIME_SLOT_TAG		= "$PREV_TIME_SLOT";
	private static final String GID_TAG					= "$GID$";
	private static final String PREV_GID_TAG			= "$PREV_GID$";
	private static final String GOAL_MODULES_TAG 		= "$GOAL_MODULES$";
	private static final String SKIPPED_TAG				= "$SKIPPED$";
	private static final String NOT_SKIPPED_TAG			= "$NOT_SKIPPED$";
	private static final String DEC_HEADER_TAG	 		= "$DEC_HEADER$";
	private static final String DEC_TYPE_TAG	 		= "$DEC_TYPE$";
	private static final String MAX_TRIES_TAG	 		= "$MAX_TRIES$";
	private static final String MAX_RETRIES_TAG	 		= "$MAX_RETRIES$";
	private static final String CARD_N_TAG		 		= "$CARD_N$";
	private static final String XOR_CTX_TAG				= "$XOR_CTX$";
	private static final String REWARD_TAG				= "$REWARD_STRUCTURE$";
	private static final String COST_VALUE_TAG			= "$COST$";
	private static final String CONST_PARAM_TAG			= "$CONST_PARAM$";

	private final String constOrParam;

	/** where to find PRISM related template base section, inside the template folder */
	private final String TEMPLATE_PRISM_BASE_PATH = "PRISM/";

	private String templateInputBaseFolder;
	/** template input PRISM folder */
	private String inputPRISMFolder;
	/** generated agent target folder */
	private String agentOutputFolder;
	/** the folder that will contain all the generated agent */
	private String basicOutputFolder;
	/** the base package for the current Agent */
	private String basicAgentPackage;

	// Strings that contain the parts of the ADF skeleton, read from file
	private String header, body, reward;
	private StringBuilder planModules = new StringBuilder();
	private StringBuilder rewardModule = new StringBuilder();

	/** PRISM patterns */
	private String leafGoalPattern;
	private String andDecPattern;
	private String xorDecPattern;
	private String xorDecHeaderPattern;
	private String xorNotSkippedPattern;
	private String seqRenamePattern;
	private String trySDecPattern;
	private String tryFDecPattern;
	private String optDecPattern;
	private String optHeaderPattern;
	private String ctxHeaderPattern;
	private String ctxFailPattern;
	private String ctxSkipPattern;
	private String seqCardPattern;
	private String intlCardPattern;
	private String rtryCardPattern;
	private String ctxTrySPattern;
	private String ctxTryFPattern;
	private String rewardPattern;
	private String ndPattern;
	private String ndHeaderPattern;
	private String ndBodyPattern;

	/** Has all the informations about the agent. */ 
	private AgentDefinition ad;
	/** the list of plan that are root for a capability of the selected agent */
	private List<Plan> capabilityPlanList;

	private List<String> rewardVariables = new ArrayList<String>();
	
	private int nonDeterminismCtxId = 1;
	private Map<RTContainer,String> nonDeterminismCtxList = new HashMap<RTContainer,String>();
	/**
	 * Creates a new AgentWriter instance
	 * 
	 * @param ad the source agentDefinition object from which data should be extracted
	 * @param inputFolder template input folder 
	 * @param outputFolder generated code target folder
	 */
	public PrismWriter(AgentDefinition ad, List<Plan> capPlan, String input, String output, Boolean parametric) {
		this.ad = ad;
		this.capabilityPlanList = capPlan;
		this.templateInputBaseFolder = input + "/"; 
		this.inputPRISMFolder = templateInputBaseFolder + TEMPLATE_PRISM_BASE_PATH;
		this.basicOutputFolder = output + "/";
		this.agentOutputFolder = 
				basicOutputFolder + PathLocation.BASIC_AGENT_PACKAGE_PREFIX + ad.getAgentName() + "/";

		//the package that generated bdi .java files will be put in
		this.basicAgentPackage = PathLocation.BASIC_AGENT_PACKAGE_PREFIX + ad.getAgentName();

		this.constOrParam = parametric ? "param" : "const";
	}

	/**	private List<Plan> capabilityPlanList;

	 * Writes the whole Agent (ADF + Java plan bodies).
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */
	public void writeModel() throws CodeGenerationException, IOException {
		String utilPkgName = basicAgentPackage + PathLocation.UTIL_KL_PKG;

		String prismInputFolder = inputPRISMFolder;

		String planOutputFolder = agentOutputFolder + "plans" + "/";
		String planPkgName = basicAgentPackage + ".plans";

		header = ManageWriter.readFileAsString( prismInputFolder + "modelheader.nm" );
		body = ManageWriter.readFileAsString( prismInputFolder + "modelbody.nm" );
		reward = ManageWriter.readFileAsString( prismInputFolder + "modelreward.nm" );

		//create the model output dir
		writeAnOutputDir( agentOutputFolder );

		//create the output PRISM file
		PrintWriter modelFile = ManageWriter.createFile(ad.getAgentName() + ".nm", agentOutputFolder);
		writePrismModel( prismInputFolder, ad.rootlist, planOutputFolder, basicAgentPackage, utilPkgName, planPkgName );		
		printModel( modelFile );
	}

	/**
	 * Writes all goals to the ADF file (to beliefbase, goals and plans section) and organizes
	 * (copies) the plan bodies. Works not recursive on the goal structure, but processes all goals
	 * in the list in sequence.
	 * 
	 * @param input the template input folder
	 * @param gb beliefe base goal
	 * @param planOutputFolder
	 * @param pkgName
	 * @param utilPkgName
	 * @param planPkgName 
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */
	private void writePrismModel( String input, LinkedList<GoalContainer> rootGoals, 
			String planOutputFolder, String pkgName, String utilPkgName, String planPkgName ) throws CodeGenerationException, IOException {

		leafGoalPattern 				= ManageWriter.readFileAsString(input + "pattern_leafgoal.nm");
		andDecPattern 					= ManageWriter.readFileAsString(input + "pattern_and.nm");
		xorDecPattern 					= ManageWriter.readFileAsString(input + "pattern_xor.nm");
		xorDecHeaderPattern 			= ManageWriter.readFileAsString(input + "pattern_xor_header.nm");
		xorNotSkippedPattern	 		= ManageWriter.readFileAsString(input + "pattern_skip_not_xor.nm");
		seqRenamePattern				= ManageWriter.readFileAsString(input + "pattern_seq_rename.nm");
		trySDecPattern	 				= ManageWriter.readFileAsString(input + "pattern_try_success.nm");
		tryFDecPattern	 				= ManageWriter.readFileAsString(input + "pattern_try_fail.nm");
		optDecPattern 					= ManageWriter.readFileAsString(input + "pattern_opt.nm");
		optHeaderPattern	 			= ManageWriter.readFileAsString(input + "pattern_opt_header.nm");
		ctxHeaderPattern				= ManageWriter.readFileAsString(input + "pattern_ctx_header.nm");
		ctxSkipPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_skip.nm");
		ctxFailPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_fail.nm");
		seqCardPattern	 				= ManageWriter.readFileAsString(input + "pattern_card_seq.nm");
		intlCardPattern	 				= ManageWriter.readFileAsString(input + "pattern_card_intl.nm");
		rtryCardPattern	 				= ManageWriter.readFileAsString(input + "pattern_card_retry.nm");
		rewardPattern					= ManageWriter.readFileAsString(input + "pattern_reward.nm");
		ctxTrySPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_try_success.nm");
		ctxTryFPattern					= ManageWriter.readFileAsString(input + "pattern_ctx_try_fail.nm");
		ndPattern						= ManageWriter.readFileAsString(input + "pattern_nondeterminism.nm");
		ndHeaderPattern					= ManageWriter.readFileAsString(input + "pattern_nd_header.nm");
		ndBodyPattern					= ManageWriter.readFileAsString(input + "pattern_nd_body.nm");

		Collections.sort(rootGoals);

		for( GoalContainer root : rootGoals ) {
			writeElement(
					root, 
					leafGoalPattern,							
					null);
			planModules = planModules.append("label \"success\" = " + root.getClearElId() + ";");
		}
	}

	/**
	 * Writes the dispatch plans (with bodies) for every child goal
	 * 
	 * @param goal
	 * @param pattern
	 * @throws IOException 
	 */
	private String[] writeElement(
			RTContainer root, 
			String pattern, 							 
			String prevFormula) throws IOException {
		
		if (root.isDecisionMaking()) {
			writeNondeterministicModule(root);
		}
		
		String operator = root.getDecomposition() == Const.AND ? " & " : " | ";
		if(!root.getDecompGoals().isEmpty()){
			StringBuilder goalFormula = new StringBuilder();
			String prevGoalFormula = prevFormula;
			int prevTimeSlot = root.getDecompGoals().get(0).getRootTimeSlot();
			
			for(GoalContainer gc : root.getDecompGoals()){
				String currentFormula;
				if(prevTimeSlot < gc.getRootTimeSlot())
					currentFormula = prevGoalFormula;
				else
					currentFormula = prevFormula;
				writeElement(gc, pattern, currentFormula);												
				if(gc.isIncluded())
					prevGoalFormula = gc.getClearElId();
				if(prevGoalFormula != null)
					goalFormula.append(prevGoalFormula + operator);							
			}
			if(prevGoalFormula != null)
				goalFormula.replace(goalFormula.lastIndexOf(operator), goalFormula.length(), "");
			if(root.isIncluded()) {
				planModules = planModules.append("formula " + root.getClearElId() + " = " + goalFormula + ";\n");
			}
			return new String [] {root.getClearElId(), goalFormula.toString()};
		}else if(!root.getDecompPlans().isEmpty()){
			StringBuilder taskFormula = new StringBuilder();
			String prevTaskFormula = prevFormula;
			
			for(PlanContainer pc : root.getDecompPlans()){
				String childFormula = writeElement(pc, pattern, prevTaskFormula)[1];
				//prevTaskFormula = pc.getClearElId();
				if(!childFormula.isEmpty())
					taskFormula.append("(" + childFormula + ")" + operator);
			}
			if(taskFormula.length() > 0)
				taskFormula.replace(taskFormula.lastIndexOf(operator), taskFormula.length(), "");
			if(root instanceof GoalContainer)
				planModules = planModules.append("formula " + root.getClearElId() + " = " + taskFormula + ";\n\n");
			return new String [] {root.getClearElId(), taskFormula.toString()};
		}else if(root instanceof PlanContainer){
			writeRewardModule(root);
			return writePrismModule(root, pattern, prevFormula);
		}

		return new String[]{"",""};
	}

	private void writeNondeterministicModule(RTContainer root) throws ParseCancellationException, IOException {
		String singlePattern = new String(this.ndPattern);

		Map<String, String> mapContexts = getContextList(root);
		
		List<String[]> list = new ArrayList<String[]>();
		GenerateCombination comb1 = new GenerateCombination(mapContexts.keySet().toArray(new String[0]), 0) ;
        while (comb1.hasNext()) {
        	list.add(comb1.next());
        }
        
        StringBuilder sbHeader = new StringBuilder();
    	StringBuilder sbType = new StringBuilder();
    	int nextState = 1;
        
    	
    	String sbHeaderAux = "\n";
    	String sbTypeAux = new String();
    	String contextUpdate = new String();
    	
        for (String[] ctxCombination : list) {
        	String ndHeaderPattern = new String(this.ndHeaderPattern);
        	ndHeaderPattern = ndHeaderPattern.replace("$N$", Integer.toString(this.nonDeterminismCtxId));
        	sbHeader.append(ndHeaderPattern + " //");
        	
        	//comenta os contextos envolvidos
        	contextUpdate = new String();
        	for (Map.Entry<String, String> entry : mapContexts.entrySet()) {
        		String global = "global CTX_" + entry.getKey()+ ": [0..1] init 0;\n";
        		if (!sbHeaderAux.contains(global)) sbHeaderAux = sbHeaderAux.concat(global); 
        			
        		for (int i = 0; i < ctxCombination.length; i++) {
        			if (ctxCombination[i].equals(entry.getKey())) {
            			sbHeader.append(" " + entry.getValue() + " &");
            			//gravo os contextos que vão ser setados
            			contextUpdate = contextUpdate.concat(" & (CTX_" + entry.getKey() + "'=1)");
            		}
        		}
        	}
        	sbHeader.replace(sbHeader.length()-1, sbHeader.length(), "\n");
        	
        	//cria a linha de transição não-determinística (sucesso: estado nextState);
        	String ndBodyPattern = new String(this.ndBodyPattern);
        	ndBodyPattern = ndBodyPattern.replace("$N$", Integer.toString(this.nonDeterminismCtxId));
        	ndBodyPattern = ndBodyPattern.replace("$NEXT_STATE$", Integer.toString(nextState));
        	
        	String ndBodyAux = "\t[] s$GID$ = $N$ -> (s$GID$'=$MAX_ND$)$CONTEXT_UPDATE$;\n";
        	ndBodyAux = ndBodyAux.replace("$N$", Integer.toString(this.nonDeterminismCtxId));
        	ndBodyAux = ndBodyAux.replace("$CONTEXT_UPDATE$", contextUpdate);
        	sbTypeAux = sbTypeAux.concat(ndBodyAux);
        	
        	if (sbType.length() == 0) sbType.append(ndBodyPattern);
        	else sbType.append("\t" + ndBodyPattern);
        	
        	this.nonDeterminismCtxId++;
        	nextState++;
        }        
        sbHeader.append(sbHeaderAux);
        sbTypeAux = sbTypeAux.replace("$MAX_ND$", Integer.toString(nextState));

        singlePattern = singlePattern.replace("$MAX_ND$", Integer.toString(nextState));
        singlePattern = singlePattern.replace("$FINAL_TYPE$", sbTypeAux);
        singlePattern = singlePattern.replace(DEC_HEADER_TAG, sbHeader.toString());
        singlePattern = singlePattern.replace(DEC_TYPE_TAG, sbType.toString());
        singlePattern = singlePattern.replace(GID_TAG, root.getClearElId());
        
    	//Time
    	Integer prevTimePath = root.getPrevTimePath();
    	Integer timePath = root.getTimePath();
    	Integer timeSlot = root.getTimeSlot()-1;
    	if(root.getCardType().equals(Const.SEQ))
    		timeSlot -= root.getCardNumber() - 1; 
    	for(int i = root.getCardNumber(); i >= 0; i--){
    		singlePattern = singlePattern.replace(PREV_TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", prevTimePath + "_" + (timeSlot - 1 + i) + "");
    		singlePattern = singlePattern.replace(TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", timePath + "_" + (timeSlot + i) + "");
    	}
        
        planModules = planModules.append(singlePattern+"\n");
	}
	
	private Map<String, String> getContextList(RTContainer root) throws ParseCancellationException, IOException {
		Map<String, String> context = new TreeMap<String, String>();
		
		StringBuilder fulfillmentConditions = new StringBuilder();
		String id = new String();

		if (!root.getDecompGoals().isEmpty()) {
			for (GoalContainer goal : root.getDecompGoals()) {
				fulfillmentConditions = getContextsInfo(goal);
				id = goal.getClearElId();
				
				this.nonDeterminismCtxList.put(goal, fulfillmentConditions.toString());
				context.put(id, fulfillmentConditions.toString());
			}
		}
		else {
			for (PlanContainer plan : root.getDecompPlans()) {
				fulfillmentConditions = getContextsInfo(plan);				
				id = plan.getClearElId();
				
				this.nonDeterminismCtxList.put(plan, fulfillmentConditions.toString());
				context.put(id, fulfillmentConditions.toString());
			}
		}
		return context;
	}

	private void writeRewardModule(RTContainer root) {
		PlanContainer plan = (PlanContainer) root;
		if (plan.getCostRegex() != null) {
			String rewardPattern = new String(this.rewardPattern);
			rewardPattern = rewardPattern.replace(GID_TAG, plan.getClearElId());
			String costVariable = plan.getCostVariable();
			if (costVariable == null) {
				rewardPattern = rewardPattern.replace(COST_VALUE_TAG, plan.getCostValue());
			}
			else {
				rewardPattern = rewardPattern.replace(COST_VALUE_TAG, plan.getCostValue() + "*" + costVariable);
				if (!this.rewardVariables.contains(costVariable)) this.rewardVariables.add(costVariable);
			}
			rewardModule = rewardModule.append(rewardPattern);
		}
	}

	private String[] writePrismModule(
			RTContainer root, 
			String singlePattern,							
			String prevFormula) throws IOException{
	
		singlePattern = new String(singlePattern);
	
		String seqCardPattern = new String(this.seqCardPattern),
				intlCardPattern = new String(this.intlCardPattern),
				rtryCardPattern = new String(this.rtryCardPattern),
				andDecPattern = new String(this.andDecPattern),
				xorDecPattern = new String(this.xorDecPattern),
				xorDecHeaderPattern = new String(this.xorDecHeaderPattern),
				trySDecPattern = new String(this.trySDecPattern),
				tryFDecPattern = new String(this.tryFDecPattern),
				optHeaderPattern = new String(this.optHeaderPattern),
				optDecPattern = new String(this.optDecPattern),
				ctxTrySPattern = new String(this.ctxTrySPattern),
				ctxTryFPattern = new String(this.ctxTryFPattern),
				ctxSkipPattern = new String(this.ctxSkipPattern),
				ctxFailPattern = new String(this.ctxFailPattern);
	
		PlanContainer plan = (PlanContainer) root;
		String planModule;
		StringBuilder planFormula = new StringBuilder();
	
		boolean contextPresent = false;
		boolean goalContext = false;
		boolean nonDeterminismCtx = false;
	
		if(constOrParam.equals("const") &&
				(!plan.getFulfillmentConditions().isEmpty() ||
						!plan.getAdoptionConditions().isEmpty())){
	
			contextPresent = true;
	
			for (String ctxCondition : plan.getFulfillmentConditions()){
				Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
				if (((CtxSymbols) parsedCtxs[2] == CtxSymbols.COND) || (plan.getClearElId().contains("X"))) 
					goalContext = true; 
			}
			
			String ctx = getContextsInfo(plan).toString();
			RTContainer node = getKeyRTContainer(this.nonDeterminismCtxList,ctx);
			if (this.nonDeterminismCtxList.containsValue(ctx) && (node.equals(plan) || equalsRoot(node, plan))) {
				nonDeterminismCtx = true;
			}
		}
	
		if(plan.getCardNumber() > 1){
			StringBuilder seqRenames = new StringBuilder();
			if(plan.getCardType() == Const.SEQ){
				for(int i = 2; i <= plan.getCardNumber(); i++){
					String seqRename = new String(seqRenamePattern);
					seqRename = seqRename.replace(CARD_N_TAG, i + "");
					seqRenames.append(seqRename);
				}
				seqCardPattern = seqCardPattern.replace("$SEQ_RENAMES$", seqRenames);
				planModule = seqCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
			}else if (plan.getCardType() == Const.RTRY){
				planModule = rtryCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
			}
			else{
				for(int i = 2; i <= plan.getCardNumber(); i++){
					String seqRename = new String(seqRenamePattern);
					seqRename = seqRename.replace(CARD_N_TAG, i + "");
					seqRenames.append(seqRename);
				}
				intlCardPattern = intlCardPattern.replace("$SEQ_RENAMES$", seqRenames);
				planModule = intlCardPattern.replace(MODULE_NAME_TAG, plan.getClearElName());
			}
		}else
			planModule = singlePattern.replace(MODULE_NAME_TAG, plan.getClearElName());
	
		StringBuilder sbHeader = new StringBuilder();
		StringBuilder sbType = new StringBuilder();
	
		if((plan.getTryOriginal() != null || plan.getTrySuccess() != null || plan.getTryFailure() != null) ||
				(!plan.getAlternatives().isEmpty() || !plan.getFirstAlternatives().isEmpty()) ||
				(plan.isOptional())){			
			if(plan.getTryOriginal() != null || plan.getTrySuccess() != null || plan.getTryFailure() != null){
				if(plan.getTrySuccess() != null || plan.getTryFailure() != null){
					//Try					
					if(plan.getAlternatives().isEmpty() && plan.getFirstAlternatives().isEmpty()){
						if(contextPresent){
							sbHeader.append(getContextHeader(plan));
							sbType.append(ctxFailPattern);
						}
						else {
							sbType.append(andDecPattern);
						}
					}
					processPlanFormula(plan, planFormula, Const.TRY, nonDeterminismCtx);
				}else if(plan.isSuccessTry()){
					//Try success
					PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
					if(contextPresent){
						sbHeader.append(getContextHeader(plan));
						ctxTrySPattern = ctxTrySPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
						sbType.append(ctxTrySPattern);
					}
					else {
						trySDecPattern = trySDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
						sbType.append(trySDecPattern);
					}
					processPlanFormula(plan, planFormula, Const.TRY_S, nonDeterminismCtx);
				}else{
					//Try fail
					PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
					if(contextPresent){
						sbHeader.append(getContextHeader(plan));
						ctxTryFPattern = ctxTryFPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
						sbType.append(ctxTryFPattern);
					}
					else {
						tryFDecPattern = tryFDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
						sbType.append(tryFDecPattern);
					}
					processPlanFormula(plan, planFormula, Const.TRY_F, nonDeterminismCtx);
				}	
			}
			if(!plan.getAlternatives().isEmpty() || !plan.getFirstAlternatives().isEmpty()){
				//Alternatives
				String xorNotSkippeds = new String();
				StringBuilder xorHeaders = new StringBuilder();
				String xorNotSkipped = new String(xorNotSkippedPattern);
				String xorOrCtx = new String();
	
				if(!plan.getAlternatives().isEmpty()){
					for(RTContainer altFirst : plan.getAlternatives().keySet()){
						String xorVar = new String(xorDecHeaderPattern);
						xorOrCtx = updateXorOrCtx(altFirst);
						xorVar = xorVar.replace(XOR_CTX_TAG, xorOrCtx);
						if (contextPresent) xorVar = commentContextInformation(altFirst, xorVar);
						xorHeaders.append(xorVar.replace(GID_TAG, altFirst.getClearElId()));
						xorNotSkipped = xorNotSkipped.replace(XOR_CTX_TAG, xorOrCtx);
						xorNotSkippeds = xorNotSkipped.replace(GID_TAG, altFirst.getClearElId());
	
						LinkedList<RTContainer> alts = plan.getAlternatives().get(altFirst);
						for(RTContainer alt : alts){
							xorVar = new String(xorDecHeaderPattern);
							xorOrCtx = updateXorOrCtx(alt);
							xorVar = xorVar.replace(XOR_CTX_TAG, xorOrCtx);
							if (contextPresent) xorVar = commentContextInformation(alt, xorVar);
							xorHeaders.append(xorVar.replace(GID_TAG, alt.getClearElId()));
						}
					}
					sbHeader.append(xorHeaders);
					processPlanFormula(plan, planFormula, Const.XOR, nonDeterminismCtx);
				}
				if(!plan.getFirstAlternatives().isEmpty()){
					for (RTContainer firstAlt : plan.getFirstAlternatives()) {
						for (RTContainer alt : firstAlt.getAlternatives().get(firstAlt)) {
							if (alt.equals(plan) || equalsRoot(alt,plan)) {
								xorNotSkipped = new String(xorNotSkippedPattern);
								xorOrCtx = updateXorOrCtx(alt);
								xorNotSkippeds = xorNotSkippeds.trim();
								xorNotSkipped = xorNotSkipped.replace(XOR_CTX_TAG, xorOrCtx);
								xorNotSkippeds = xorNotSkipped.replace(GID_TAG, alt.getClearElId());
							}
						}
					}
					processPlanFormula(plan, planFormula, Const.XOR, nonDeterminismCtx);
				}
	
				xorNotSkippeds = xorNotSkippeds.replaceAll("[\n]", "");
				xorDecPattern = xorDecPattern.replace(NOT_SKIPPED_TAG, xorNotSkippeds);
				sbType.append(xorDecPattern.replace(SKIPPED_TAG, "(1 - " + xorNotSkippeds + ")"));
			}
			if(plan.isOptional()){
				//Opt
				if (contextPresent) optHeaderPattern = commentContextInformation(plan, optHeaderPattern);
				sbHeader.append(optHeaderPattern);
				sbType.append(optDecPattern);
				processPlanFormula(plan, planFormula,Const.OPT, nonDeterminismCtx);
			}
		}else{
			//And/OR
			if(contextPresent){
				String ctxId = getContextId(plan);
				if (nonDeterminismCtx) {
					sbType.append(ctxSkipPattern.replace("$CTX_GID$", "CTX_" + ctxId));
				}
				else {
					sbHeader.append(getContextHeader(plan));
					if (goalContext) sbType.append(ctxSkipPattern.replace("$CTX_GID$", "CTX_" + ctxId));
					else sbType.append(ctxFailPattern);
				}
			}
			else {
				sbType.append(andDecPattern);
			}
			processPlanFormula(plan, planFormula, plan.getRoot().getDecomposition(), nonDeterminismCtx);
		}
	
		//Header
		planModule = planModule.replace(DEC_HEADER_TAG, sbHeader.toString());
		//Type
		planModule = planModule.replace(DEC_TYPE_TAG, sbType.toString());
	
		//Time
		Integer prevTimePath = plan.getPrevTimePath();
		Integer timePath = plan.getTimePath();
		Integer timeSlot = plan.getTimeSlot();
		if(plan.getCardType().equals(Const.SEQ))
			timeSlot -= plan.getCardNumber() - 1; 
		for(int i = plan.getCardNumber(); i >= 0; i--){
			planModule = planModule.replace(PREV_TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", prevTimePath + "_" + (timeSlot - 1 + i) + "");
			planModule = planModule.replace(TIME_SLOT_TAG + (i > 1 ? "_N" + i : "") + "$", timePath + "_" + (timeSlot + i) + "");
		}
	
		//GID
		planModule = planModule.replace(GID_TAG, plan.getClearElId());
		//CONST OR PARAM
		planModule = planModule.replace(CONST_PARAM_TAG, constOrParam);
		//MAX RETRIES
		planModule = planModule.replace(MAX_TRIES_TAG, plan.getCardNumber() + 1 + "");				
		planModule = planModule.replace(MAX_RETRIES_TAG, plan.getCardNumber() + "");
		planModules = planModules.append(planModule+"\n");				
		return new String[]{plan.getClearElId(), planFormula.toString()};
	}

	private RTContainer getKeyRTContainer(Map<RTContainer, String> list, String ctx) {
	
		for (Entry<RTContainer, String> entry : list.entrySet()) {
			if (ctx.equals(entry.getValue())) return entry.getKey();
		}
		return null;
	}
	
	private String getContextHeader(PlanContainer plan) throws ParseCancellationException, IOException {
		String header = new String(ctxHeaderPattern);
		header = commentContextInformation((RTContainer) plan, header);
		return header;
	}

	private String updateXorOrCtx(RTContainer node) {
		if (!node.getFulfillmentConditions().isEmpty()) return "CTX";
		return "XOR";
	}
	
	/** Return list contains:
	 *  [0] : fulfillmentCondition
	 *  [1] : adoptionCondition
	 *  //[2] : ctxEffect 
	 */
	private StringBuilder getContextsInfo(RTContainer altFirst) throws ParseCancellationException, IOException {
	
		StringBuilder fullContextCondition = new StringBuilder();
	
		if(!altFirst.getFulfillmentConditions().isEmpty()){				
			for(String ctxCondition : altFirst.getFulfillmentConditions()){
				Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
				fullContextCondition.append(fullContextCondition.length() > 0 ? " & " : "").append(parsedCtxs[1]);
			}		
		}
		return fullContextCondition;
	}

	private String commentContextInformation(RTContainer altFirst, String xorVar) throws ParseCancellationException, IOException {
		StringBuilder contextInformation = getContextsInfo(altFirst);
		xorVar = xorVar.substring(0, xorVar.length() - 2);
		xorVar += " //" + contextInformation.toString() + "\n";
		return xorVar;
	}
	
	/*Check if plan descends from alt*/
	private boolean equalsRoot(RTContainer alt, RTContainer plan) {
	
		RTContainer root = plan.getRoot();
		while (!root.equals(this.ad.getRootGoalList().get(0))) {
			if (alt.equals(root)) return true;
			root = root.getRoot();
		}
	
		if (alt.equals(root)) return true;
		return false;
	}

	private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType, boolean nonDeterminismCtx) throws IOException{
	
		String op = planFormula.length() == 0 ? "" : " & ";
		switch(decType){
		case OR: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType, nonDeterminismCtx));break;
		case AND: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType, nonDeterminismCtx));break;
		case XOR: planFormula.append(buildXorSuccessFormula(plan, planFormula, nonDeterminismCtx));break;
		case TRY: planFormula.append(buildTryOriginalFormula(plan, planFormula, decType, false, nonDeterminismCtx));break;
		case TRY_S: break;
		case TRY_F: break;
		case OPT: planFormula.append(buildOptFormula(plan, planFormula));break;					  
		default: planFormula.append(op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx));
		}
	}
	
	private String buildAndOrSuccessFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean nonDeterminismCtx) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		switch(decType){
		case AND: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx);
		case OR: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan, nonDeterminismCtx);
		default: return "";
		}		
	}

	private String buildOptFormula(RTContainer plan, StringBuilder planFormula) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		return op + "(s" + plan.getClearElId() + "=2 | s" + plan.getClearElId() + "=3)";
	}
	
	private String buildTryOriginalFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv, boolean nonDeterminismCtx) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		return 	op  + "("
		+ "(s" + plan.getClearElId() + "=2 & " 
		+ buildTrySuccessFailureFormula(plan.getTrySuccess(), planFormula, Const.TRY_S, false)
		+ ") | "
		+ "(s" + plan.getClearElId() + "=4 & " 
		+ buildTrySuccessFailureFormula(plan.getTryFailure(), planFormula, Const.TRY_F, false)
		+ ")"
		+ buildContextSuccessFormula(plan, nonDeterminismCtx)
		+ ")";
	}
	
	private String buildTrySuccessFailureFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv) throws IOException{
		switch(decType){
		case TRY_S: return  plan != null ? "s" + plan.getClearElId() + "=" + (!inv ? "2" : "3") : (!inv ? "true" : "true");
		case TRY_F: return  plan != null ? "s" + plan.getClearElId() + "=2" : "true";
		default: return "";
		}		
	}

	private String buildXorSuccessFormula(PlanContainer plan, StringBuilder planFormula, boolean nonDeterminismCtx) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("s" + plan.getClearElId() + "=2 & ");
	
		if (!plan.getAlternatives().isEmpty()) {
			for(RTContainer altFirst: plan.getAlternatives().keySet())
				for(RTContainer alt : plan.getAlternatives().get(altFirst))
					for(RTContainer decAlt : RTContainer.fowardMeansEnd(alt, new LinkedList<RTContainer>()))
						sb.append("s" + decAlt.getClearElId() + "=3 & ");
		}
	
		if (!plan.getFirstAlternatives().isEmpty()) {
			for(RTContainer firstAlt: plan.getFirstAlternatives()){
				//Append the first node of alternatives
				for(RTContainer decAlt : RTContainer.fowardMeansEnd(firstAlt, new LinkedList<RTContainer>()))
					if (!decAlt.equals(plan))
						sb.append("s" + decAlt.getClearElId() + "=3 & ");
	
				//Append other nodes
				for(RTContainer alt : firstAlt.getAlternatives().get(firstAlt))
					for(RTContainer decAlt : RTContainer.fowardMeansEnd(alt, new LinkedList<RTContainer>()))
						if (!decAlt.equals(plan))
							sb.append("s" + decAlt.getClearElId() + "=3 & ");
			}
		}
	
		sb.replace(sb.lastIndexOf(" & "), sb.length(), "");
		return sb.toString();
	}

	private String buildContextSuccessFormula(RTContainer plan, boolean nonDeterminismCtx) throws IOException{
	
		if(this.constOrParam.equals("param"))
			return "";	
	
		StringBuilder sb = new StringBuilder();		
		for(String ctxCondition : plan.getFulfillmentConditions()) {
			Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
			if((CtxSymbols)parsedCtxs[2] == CtxSymbols.COND || nonDeterminismCtx || plan.getClearElId().contains("X")){
				sb.append(sb.length() > 0 ? " | " : "").append("CTX_" + getContextId(plan) + "=0");
			}
		}
		if(sb.length() > 0)
			sb.insert(0, " | (s" + plan.getClearElId() + "=3 & ").append(")");
		return sb.toString();
	}
	
	private String getContextId(RTContainer plan) throws ParseCancellationException, IOException {
		String ctx = getContextsInfo(plan).toString();
		RTContainer node = getKeyRTContainer(this.nonDeterminismCtxList,ctx);
		
		if (this.nonDeterminismCtxList.containsValue(ctx) && (equalsRoot(node, plan))) {
			return node.getClearElId();
		}
		return plan.getClearElId();
	}

	/**
	 * Create an agent output dir
	 * 
	 * @param output the dir to be created
	 * 
	 * @throws CodeGenerationException
	 */
	private void writeAnOutputDir( String output ) throws CodeGenerationException {
		File dir = new File( output );
	
		if( !dir.exists() && !dir.mkdirs() ) {
			String msg = "Error: Can't create directory \"" + dir + "\"!";
			ATCConsole.println( msg );
			throw new CodeGenerationException( msg );
		}
	}

	/**
	 * replaces all placeholders in the ADF skeleton and writes the ADF file.
	 * 
	 * @param adf the file to be written to
	 */
	private void printModel( PrintWriter adf ) {
	
		body = body.replace(GOAL_MODULES_TAG, planModules);
		reward = declareRewardVariable() + "\n" + reward.replace(REWARD_TAG, rewardModule);
	
		String model = header + "\n" + body + reward;
		ManageWriter.printModel(adf, model);
	}
	
	private String declareRewardVariable() {
		StringBuilder variables = new StringBuilder();
		for (String var : this.rewardVariables) {
			variables.append("\nconst double " + var + ";");
		}
		return variables.toString();
	}
}