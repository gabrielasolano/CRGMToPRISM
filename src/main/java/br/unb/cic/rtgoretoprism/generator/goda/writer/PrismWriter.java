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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	//	private static final String UTIL_PACKAGE_TAG		= "$UTIL_PACKAGE";
	//	private static final String CAPABILITY_AGENT_TAG	= "$CAPABILITY_AGENT";


	private static final String MODULE_NAME_TAG			= "$MODULE_NAME$";
	private static final String NO_ERROR_TAG			= "$NO_ERROR$";
	private static final String TIME_SLOT_TAG			= "$TIME_SLOT";
	private static final String PREV_TIME_SLOT_TAG		= "$PREV_TIME_SLOT";
	private static final String GID_TAG					= "$GID$";
	private static final String DEFAULT_VAL				= "$DEFAULT_VAL$";
	private static final String PREV_GID_TAG			= "$PREV_GID$";
	private static final String GOAL_MODULES_TAG 		= "$GOAL_MODULES$";
	private static final String SKIPPED_TAG				= "$SKIPPED$";
	private static final String NOT_SKIPPED_TAG			= "$NOT_SKIPPED$";
	/*	private static final String XOR_GIDS_TAG 			= "$XOR_GIDS$";
	private static final String XOR_VALUE_TAG	 		= "$XOR_VALUE$";*/
	private static final String DEC_HEADER_TAG	 		= "$DEC_HEADER$";
	private static final String DEC_TYPE_TAG	 		= "$DEC_TYPE$";
	//	private static final String CARD_TYPE_TAG	 		= "$CARD_TYPE$";
	private static final String MAX_TRIES_TAG	 		= "$MAX_TRIES$";
	private static final String MAX_RETRIES_TAG	 		= "$MAX_RETRIES$";
	private static final String CARD_N_TAG		 		= "$CARD_N$";
	private static final String XOR_CTX_TAG				= "$XOR_CTX$";

	private static final String CTX_CONDITION_TAG		= "$CTX_CONDITION$";
	private static final String CTX_EFFECT_TAG			= "$CTX_EFFECT$";

	private static final String CONST_PARAM_TAG			= "$CONST_PARAM$";

	private static final String PARAMS_BASH_TAG	 		= "$PARAMS_BASH$";
	private static final String REPLACE_BASH_TAG	 	= "$REPLACE_BASH$";

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
	private String header, body, evalBash;

	// Strings filled with content, to replace the placeholders in the adf skeleton.
	//Note: they are used whith concat() function


	private String noErrorFormula = "";
	private StringBuilder planModules = new StringBuilder();
	private String evalFormulaParams = "";
	private String evalFormulaReplace = "";

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
	private String seqCardPattern;
	private String intlCardPattern;
	private String ctxGoalPattern;
	private String ctxTaskPattern;
	private String prevFailurePattern;

	/** Has all the informations about the agent. */ 
	private AgentDefinition ad;
	/** the list of plan that are root for a capability of the selected agent */
	private List<Plan> capabilityPlanList;

	private Map<String, String> ctxVars;

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

		this.ctxVars = new TreeMap<String, String>();
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

		//read some of the used template

		header = ManageWriter.readFileAsString( prismInputFolder + "modelheader.pm" );
		body = ManageWriter.readFileAsString( prismInputFolder + "modelbody.pm" );
		evalBash = ManageWriter.readFileAsString( prismInputFolder + "eval_formula.sh" );
		//footer = readFileAsString( planInputFolder + "agentfooter.xml" );

		//update some template placeholder
		//header = header.replace( NAME_TAG, ad.getAgentName() );
		//header = header.replace( PACKAGE_TAG, basicAgentPackage );
		//header = header.replace( UTIL_PACKAGE_TAG, utilPkgName );
		//header = header.replace( BDI_PLAN_PACKAGE_TAG, planPkgName );


		//create the model output dir
		writeAnOutputDir( agentOutputFolder );
		//create the output PRISM file */

		PrintWriter modelFile = ManageWriter.createFile(ad.getAgentName() + ".pm", agentOutputFolder);
		PrintWriter evalBashFile = ManageWriter.createFile("eval_formula.sh", agentOutputFolder);

		//Reads all softgoals from the softgoals list and writes them into the belief base.
		//writeBBSoftGoals( ad.softgoalbase );
		//Writes all goals to the ADF file
		writePrismModel( prismInputFolder, ad.rootlist, planOutputFolder, basicAgentPackage, utilPkgName, planPkgName );		
		//Writes the plan contributions and the bodies of the real plans
		//writePlans( planInputFolder, planOutputFolder, ad.planbase, basicAgentPackage, utilPkgName, planPkgName, ad.getAgentName() );
		//Copies to the output directory all the files where only the package-name changes.
		//writeDefaultJavaFiles( planInputFolder, planOutputFolder, basicAgentPackage, utilPkgName, planPkgName );
		//replaces all placeholders in the ADF skeleton and writes the ADF file.
		printModel( modelFile );
		printEvalBash( evalBashFile );
		//Writes the batch files (Windows) to start the agent.
		//writeAgentStartingFile( basicOutputFolder, ad.getAgentName(), basicAgentPackage );
		//Writes the batch files (Windows) to compile the agent.
		//writeAgentCompileFile( agentOutputFolder );
		//Write the platform starting file
		//writePlatformStartingFile( basicOutputFolder );
		//copy some of the input template dir that are used by the running BDI agent 
		//writeUtilDir( utilInputFolder, agentOutputFolder, utilPkgName );
		//create the skeleton part of the CL elements (the capability agent and related utils)
		//writeCapabilityCLSkeleton( ad.getAgentName(), capabilityPlanList, agentOutputFolder, templateInputBaseFolder );
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

		leafGoalPattern 				= ManageWriter.readFileAsString(input + "pattern_leafgoal.pm");
		andDecPattern 					= ManageWriter.readFileAsString(input + "pattern_and.pm");
		xorDecPattern 					= ManageWriter.readFileAsString(input + "pattern_xor.pm");
		xorDecHeaderPattern 			= ManageWriter.readFileAsString(input + "pattern_xor_header.pm");
		xorNotSkippedPattern	 		= ManageWriter.readFileAsString(input + "pattern_skip_not_xor.pm");
		seqRenamePattern				= ManageWriter.readFileAsString(input + "pattern_seq_rename.pm");
		trySDecPattern	 				= ManageWriter.readFileAsString(input + "pattern_try_success.pm");
		tryFDecPattern	 				= ManageWriter.readFileAsString(input + "pattern_try_fail.pm");
		optDecPattern 					= ManageWriter.readFileAsString(input + "pattern_opt.pm");
		optHeaderPattern	 			= ManageWriter.readFileAsString(input + "pattern_opt_header.pm");
		seqCardPattern	 				= ManageWriter.readFileAsString(input + "pattern_card_seq.pm");
		intlCardPattern	 				= ManageWriter.readFileAsString(input + "pattern_card_retry.pm");//TODO: create retry in a separate pattern
		ctxGoalPattern	 				= ManageWriter.readFileAsString(input + "pattern_ctx_goal.pm");
		ctxTaskPattern	 				= ManageWriter.readFileAsString(input + "pattern_ctx_task.pm");
		prevFailurePattern	 			= ManageWriter.readFileAsString(input + "pattern_prev_failure.pm");

		Collections.sort(rootGoals);

		for( GoalContainer root : rootGoals ) {
			writeElement(
					root, 
					leafGoalPattern,							
					null);

			StringBuilder sbCtxVars = new StringBuilder();
			for(String ctx : ctxVars.keySet())
				sbCtxVars.append(constOrParam + " " + ctxVars.get(ctx) + " " + ctx + ";\n");
			planModules = planModules.append(sbCtxVars.toString());
		}
		cleanPlanModules();
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
			if(root.isIncluded())
				planModules = planModules.append("\nformula " + root.getClearElId() + " = " + goalFormula + ";\n");
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
				planModules = planModules.append("\nformula " + root.getClearElId() + " = " + taskFormula + ";\n");
			return new String [] {root.getClearElId(), taskFormula.toString()};
		}else if(root instanceof PlanContainer){
			return writePrismModule(root, pattern, prevFormula);
		}

		return new String[]{"",""};
	}

	@SuppressWarnings("unchecked")
	private String[] writePrismModule(
			RTContainer root, 
			String singlePattern,							
			String prevFormula) throws IOException{

		singlePattern = new String(singlePattern);

		String seqCardPattern = new String(this.seqCardPattern),
				intlCardPattern = new String(this.intlCardPattern),
				andDecPattern = new String(this.andDecPattern),
				xorDecPattern = new String(this.xorDecPattern),
				xorDecHeaderPattern = new String(this.xorDecHeaderPattern),
				trySDecPattern = new String(this.trySDecPattern),
				tryFDecPattern = new String(this.tryFDecPattern),
				optHeaderPattern = new String(this.optHeaderPattern),
				optDecPattern = new String(this.optDecPattern);		

		PlanContainer plan = (PlanContainer) root;
		String planModule;
		StringBuilder planFormula = new StringBuilder();

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
			}else{
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
					if(plan.getAlternatives().isEmpty() && plan.getFirstAlternatives().isEmpty())
						sbType.append(andDecPattern);
					appendTryToNoErrorFormula(plan);
					processPlanFormula(plan, planFormula, Const.TRY);
				}else if(plan.isSuccessTry()){
					//Try success
					PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
					trySDecPattern = trySDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
					sbType.append(trySDecPattern);
					processPlanFormula(plan, planFormula, Const.TRY_S);
				}else{
					//Try fail
					PlanContainer tryPlan = (PlanContainer) plan.getTryOriginal();
					tryFDecPattern = tryFDecPattern.replace(PREV_GID_TAG, tryPlan.getClearElId());
					sbType.append(tryFDecPattern);
					processPlanFormula(plan, planFormula, Const.TRY_F);
				}	
			}
			if(!plan.getAlternatives().isEmpty() || !plan.getFirstAlternatives().isEmpty()){
				//Alternatives
				String xorNotSkippeds = new String();
				StringBuilder xorHeaders = new StringBuilder();
				String xorNotSkipped = new String(xorNotSkippedPattern);
				String xorOrCtx = updateXorOrCtx(plan);

				if(false && constOrParam.equals("param")){
					String xorVar = new String(xorDecHeaderPattern);
					evalFormulaParams += xorOrCtx + "_" + plan.getClearElId() + "=\"0\";\n";
					evalFormulaReplace += " -e \"s/" + xorOrCtx + "_" + plan.getClearElId() + "/$" + xorOrCtx + "_" + plan.getClearElId() + "/g\"";
					xorVar = xorVar.replace(XOR_CTX_TAG, xorOrCtx);
					xorVar = commentContextInformation(plan, xorOrCtx, xorVar);
					xorHeaders.append(xorVar.replace(GID_TAG, plan.getClearElId()));
					sbHeader.append(xorHeaders);
					xorNotSkippeds = xorNotSkippeds.concat(xorNotSkipped.replace(XOR_CTX_TAG, xorOrCtx));
					xorNotSkippeds = xorNotSkippeds.concat(xorNotSkipped.replace(GID_TAG, plan.getClearElId()) + "*");
				}else{

					if(!plan.getAlternatives().isEmpty()){
						for(RTContainer altFirst : plan.getAlternatives().keySet()){
							String xorVar = new String(xorDecHeaderPattern);
							evalFormulaParams += xorOrCtx + "_" + altFirst.getClearElId() + "=\"0\";\n";
							evalFormulaReplace += " -e \"s/" + xorOrCtx + "_" + altFirst.getClearElId() + "/$" + xorOrCtx + "_" + altFirst.getClearElId() + "/g\"";
							xorVar = xorVar.replace(XOR_CTX_TAG, xorOrCtx);
							xorVar = commentContextInformation(plan, xorOrCtx, xorVar);
							xorHeaders.append(xorVar.replace(GID_TAG, altFirst.getClearElId()));
							xorNotSkipped = xorNotSkipped.replace(XOR_CTX_TAG, xorOrCtx);
							xorNotSkippeds = xorNotSkipped.replace(GID_TAG, altFirst.getClearElId());

							LinkedList<RTContainer> alts = plan.getAlternatives().get(altFirst);
							for(RTContainer alt : alts){
								LinkedList<RTContainer> children = alt.getDecompLeafTask(alt);
								for (RTContainer child: children) {
									xorOrCtx = updateXorOrCtx(child);
									xorVar = new String(xorDecHeaderPattern);
									if (!evalFormulaParams.contains(xorOrCtx + "_" + alt.getClearElId() + "=\"0\";\n")) {
										evalFormulaParams += xorOrCtx + "_" + alt.getClearElId() + "=\"0\";\n";
										evalFormulaReplace += " -e \"s/" + xorOrCtx + "_" + alt.getClearElId() + "/$" + xorOrCtx + "_" + alt.getClearElId() + "/g\"";
									}
									xorVar = xorVar.replace(XOR_CTX_TAG, xorOrCtx);
									xorVar = commentContextInformation(child, xorOrCtx, xorVar);
									xorVar = xorVar.replace(GID_TAG, alt.getClearElId());
									if (!xorHeaders.toString().contains(xorVar)) {
										xorHeaders.append(xorVar);
									}
								}
							}
							//appendAlternativesToNoErrorFormula(plan);
						}
						sbHeader.append(xorHeaders);
						processPlanFormula(plan, planFormula, Const.XOR);
					}
					if(!plan.getFirstAlternatives().isEmpty()){
						for (RTContainer firstAlt : plan.getFirstAlternatives()) {
							for (RTContainer alt : firstAlt.getAlternatives().get(firstAlt)) {
								if (alt.equals(plan) || equalsRoot(alt,plan)) {
									xorNotSkipped = new String(xorNotSkippedPattern);
									xorNotSkipped = xorNotSkipped.replace(XOR_CTX_TAG, xorOrCtx);
									xorNotSkippeds = xorNotSkipped.replace(GID_TAG, alt.getClearElId());
								}
							}
						}
						sbHeader.append(xorHeaders);
						processPlanFormula(plan, planFormula, Const.XOR);
					}
				}
				xorNotSkippeds = xorNotSkippeds.trim();
				xorNotSkippeds = xorNotSkippeds.replaceAll("[\n]", "");
				xorDecPattern = xorDecPattern.replace(NOT_SKIPPED_TAG, xorNotSkippeds);
				sbType.append(xorDecPattern.replace(SKIPPED_TAG, "(1 - " + xorNotSkippeds + ")"));
			}
			if(plan.isOptional()){
				//Opt
				sbHeader.append(optHeaderPattern);
				sbType.append(optDecPattern);
				noErrorFormula += " & s" + plan.getClearElId() + " < 4";
				evalFormulaParams += "OPT_" + plan.getClearElId() + "=\"1\";\n";
				evalFormulaReplace += " -e \"s/OPT_" + plan.getClearElId() + "/$OPT_" + plan.getClearElId() + "/g\"";
				processPlanFormula(plan, planFormula,Const.OPT);
			}
		}else{
			//And/OR			
			sbType.append(andDecPattern + "\n\n");
			noErrorFormula += " & s" + plan.getClearElId() + " < 4";
			processPlanFormula(plan, planFormula, plan.getRoot().getDecomposition());
		}

		evalFormulaParams += "rTask" + plan.getClearElId() + "=\"0.999\";\n";
		evalFormulaReplace += " -e \"s/rTask" + plan.getClearElId() + "/$rTask" + plan.getClearElId() + "/g\"";		
		//Header
		planModule = planModule.replace(DEC_HEADER_TAG, sbHeader.toString() + "\n");
		//Type
		planModule = planModule.replace(DEC_TYPE_TAG, sbType.toString());
		//CONTEXT CONDITION
		if(constOrParam.equals("const") &&
				(!plan.getFulfillmentConditions().isEmpty() ||
						!plan.getAdoptionConditions().isEmpty())){

			List<StringBuilder> contextsInfo = getContextsInfo(plan);
			planModule = planModule.replace(CTX_EFFECT_TAG, contextsInfo.get(2).toString());
			planModule = planModule.replace(CTX_CONDITION_TAG, "(" + contextsInfo.get(0).append(contextsInfo.get(1)).toString() + ")" + " &");
		}else{
			planModule = planModule.replace(CTX_EFFECT_TAG, "");
			planModule = planModule.replace(CTX_CONDITION_TAG, "");
		}
		//Prev Failure Guard Condition
		planModule = planModule.replace("$PREV_EFFECT$", buildPrevFailureFormula(prevFormula));
		//Prev Success Guard Condition
		planModule = planModule.replace("$PREV_SUCCESS$", buildPrevSuccessFormula(prevFormula));
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
		//planModule = planModule.replace(GID_TAG, plan.getClearElId());
		planModule = planModule.replace(GID_TAG, plan.getClearElId());
		//DEFAULT VALUE FOR RELIABILITY
		if(constOrParam.equals("const"))
			planModule = planModule.replace(DEFAULT_VAL, "=0.99");
		else
			planModule = planModule.replace(DEFAULT_VAL, "");
		//CONST OR PARAM
		planModule = planModule.replace(CONST_PARAM_TAG, constOrParam);
		//MAX RETRIES
		planModule = planModule.replace(MAX_TRIES_TAG, plan.getCardNumber() + 1 + "");				
		planModule = planModule.replace(MAX_RETRIES_TAG, plan.getCardNumber() + "");
		planModules = planModules.append(planModule);				
		return new String[]{plan.getClearElId(), planFormula.toString()};
	}

	private String updateXorOrCtx(RTContainer node) {
		if (constOrParam.equals("const") &&
				(!node.getFulfillmentConditions().isEmpty() ||
						!node.getAdoptionConditions().isEmpty())) return "CTX";
		return "XOR";
	}

	/** Return list contains:
	 *  [0] : fulfillmentCondition
	 *  [1] : adoptionCondition
	 *  [2] : ctxEffect 
	 */
	private List<StringBuilder> getContextsInfo(RTContainer altFirst) throws ParseCancellationException, IOException {

		StringBuilder fulfillmentCondition = new StringBuilder(),
				adoptionCondition = new StringBuilder(),
				ctxEffect = new StringBuilder();
		if(!altFirst.getFulfillmentConditions().isEmpty()){				
			for(String ctxCondition : altFirst.getFulfillmentConditions()){
				Object [] parsedCtxs = CtxParser.parseRegex(ctxCondition);
			
				if (!altFirst.isAlternative()) addCtxVar((List<ContextCondition>)parsedCtxs[0]);
				
				if((CtxSymbols)parsedCtxs[2] == CtxSymbols.COND){							
					fulfillmentCondition.append(fulfillmentCondition.length() > 0 ? " & " : "")
					.append(parsedCtxs[1]);
				}else{
					adoptionCondition.append(adoptionCondition.length() > 0 ? " & " : "")
					.append(parsedCtxs[1]);
				}
			}
			if(fulfillmentCondition.length() > 0){
				String ctxGoalPattern = new String(this.ctxGoalPattern);				
				ctxGoalPattern = ctxGoalPattern.replace(CTX_CONDITION_TAG, "(" + fulfillmentCondition.toString() + ")" + " &");
				ctxEffect.append(ctxGoalPattern);
			}
			if(adoptionCondition.length() > 0){
				if(fulfillmentCondition.length() > 0){
					ctxEffect.append("\n\t");
					fulfillmentCondition.append(" & ");
				}
				String ctxTaskPattern = new String(this.ctxTaskPattern);
				ctxTaskPattern = ctxTaskPattern.replace(CTX_CONDITION_TAG, "(" + adoptionCondition.toString() + ")" + " &");									
				ctxEffect.append(ctxTaskPattern);
			}				
		}

		List<StringBuilder> ctxInformation = new ArrayList<StringBuilder>();
		ctxInformation.add(0, fulfillmentCondition);
		ctxInformation.add(1, adoptionCondition);
		ctxInformation.add(2, ctxEffect);

		return ctxInformation;
	}

	private String commentContextInformation(RTContainer altFirst, String xorOrCtx, String xorVar) throws ParseCancellationException, IOException {
		if (xorOrCtx.equals("CTX")) {
			List<StringBuilder> contextInformation = getContextsInfo(altFirst);
			xorVar = xorVar.substring(0, xorVar.length() - 2);
			xorVar += " //" + contextInformation.get(0).append(contextInformation.get(1)).toString() + "\n";
		}
		return xorVar;
	}

	/*Check if plan descends from alt*/
	private boolean equalsRoot(RTContainer alt, PlanContainer plan) {

		RTContainer root = plan.getRoot();
		while (!root.equals(this.ad.getRootGoalList().get(0))) {
			if (alt.equals(root)) return true;
			root = root.getRoot();
		}

		if (alt.equals(root)) return true;
		return false;
	}

	private void addCtxVar(List<ContextCondition> ctxs){
		String type;
		for(ContextCondition ctxCondition : ctxs){
			if(ctxCondition.getType() == CtxSymbols.BOOL){
				type = "bool";
			}else if(ctxCondition.getType() == CtxSymbols.DOUBLE){
				type = "double";
			}else{
				type = "int";
			}
			ctxVars.put(ctxCondition.getVar(), type);
		}
	}

	private void processPlanFormula(PlanContainer plan, StringBuilder planFormula, Const decType) throws IOException{

		String op = planFormula.length() == 0 ? "" : " & ";
		switch(decType){
		case OR: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType));break;
		case AND: planFormula.append(buildAndOrSuccessFormula(plan, planFormula, decType));break;
		case XOR: planFormula.append(buildXorSuccessFormula(plan, planFormula));break;
		case TRY: planFormula.append(buildTryOriginalFormula(plan, planFormula, decType, false));break;
		case TRY_S: break;
		case TRY_F: break;
		case OPT: planFormula.append(buildOptFormula(plan, planFormula));break;					  
		default: planFormula.append(op + "(s" + plan.getClearElId() + "=2)");
		}
	}

	private String buildAndOrSuccessFormula(RTContainer plan, StringBuilder planFormula, Const decType) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		switch(decType){
		case AND: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan);
		case OR: return op + "(s" + plan.getClearElId() + "=2)" + buildContextSuccessFormula(plan);
		default: return "";
		}		
	}

	private String buildOptFormula(RTContainer plan, StringBuilder planFormula) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		return op + "(s" + plan.getClearElId() + "=2 | s" + plan.getClearElId() + "=3)"
		+ buildContextSuccessFormula(plan);
	}

	private String buildTryOriginalFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv) throws IOException{
		String op = planFormula.length() == 0 ? "" : " & ";
		return 	op  + "("
		+ "(s" + plan.getClearElId() + "=2 & " 
		+ buildTrySuccessFailureFormula(plan.getTrySuccess(), planFormula, Const.TRY_S, false)
		+ ") | "
		+ "(s" + plan.getClearElId() + "=4 & " 
		+ buildTrySuccessFailureFormula(plan.getTryFailure(), planFormula, Const.TRY_F, false)
		+ ")"
		//+ buildTrySuccessFailureFormula(plan.getTrySuccess(), planFormula, Const.TRY_S, true)
		+ buildContextSuccessFormula(plan)
		+ ")";
	}

	private String buildTrySuccessFailureFormula(RTContainer plan, StringBuilder planFormula, Const decType, boolean inv) throws IOException{
		switch(decType){
		case TRY_S: return  plan != null ? "s" + plan.getClearElId() + "=" + (!inv ? "2" : "3") : (!inv ? "true" : "true");
		case TRY_F: return  plan != null ? "s" + plan.getClearElId() + "=2" : "false";
		default: return "";
		}		
	}

	private String buildXorSuccessFormula(PlanContainer plan, StringBuilder planFormula) throws IOException{
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

	private String buildContextSuccessFormula(RTContainer plan) throws IOException{

		if(this.constOrParam.equals("param"))
			return "";

		StringBuilder sb = new StringBuilder();		
		for(String ctxCondition : plan.getFulfillmentConditions())
			sb.append(sb.length() > 0 ? " | " : "(").append("!(" + CtxParser.parseRegex(ctxCondition)[1] + ")");
		if(sb.length() > 0)
			sb.insert(0, " | (s" + plan.getClearElId() + "=3 & ").append("))");
		return sb.toString();
	}

	private String buildPrevSuccessFormula(String prevFormula){
		if(prevFormula == null)
			return "";
		StringBuilder sb = new StringBuilder("(" + prevFormula);
		return sb.append(") & ").toString();
	}
	
	private String buildPrevFailureFormula(String prevFormula) {
		if (prevFormula == null)
			return "";
		return new String(this.prevFailurePattern);
	}

	/*	private void appendAlternativesToNoErrorFormula(PlanContainer plan) {
		for(RTContainer altFirst : plan.getAlternatives().keySet()){
			noErrorFormula += " & (s" + plan.getClearElId() + " < 4";
			for(RTContainer altPlan : plan.getAlternatives().get(altFirst)){
				noErrorFormula += " & s" + altPlan.getClearElId() + " < 4";			
			}
			noErrorFormula += ")";		
		}
	}*/

	private void appendTryToNoErrorFormula(PlanContainer plan) {
		noErrorFormula += " & (s" + plan.getClearElId() + " < 4 | (true ";
		if(plan.getTrySuccess() != null){
			RTContainer trySucessPlan = plan.getTrySuccess();			
			noErrorFormula += " & s" + trySucessPlan.getClearElId() + " < 4";			
		}
		if(plan.getTryFailure() != null){
			RTContainer tryFailurePlan = plan.getTryFailure();			
			noErrorFormula += " & s" + tryFailurePlan.getClearElId() + " < 4";			
		}
		noErrorFormula += "))";
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
		//header = header.replace(BBGOALS_TAG, bbgoals);
		//header = header.replace(BBSOFTGOALS_TAG, bbsoftgoals);
		//header = header.replace(DECOMP_TAG, bbdecomp);
		//header = header.replace(MEANSEND_TAG, bbmeansend);
		//header = header.replace(CONTRIB_TAG, bbcontrib);
		//header = header.replace(DEPENDENCIES_TAG, bbdepend);		

		//body = body.replace(GOALS_TAG, adfgoals);
		//body = body.replace(METAGOALS_TAG, adfmetagoals);
		//body = body.replace(REQUESTPLANS_TAG, adfrequestplans);
		//body = body.replace(DISPATCHPLANS_TAG, adfdispatchplans);
		//body = body.replace(DISPATCHANDPLANS_TAG, adfandplans);
		//body = body.replace(METAPLANS_TAG, adfmetaplans);
		//body = body.replace(REALPLANS_TAG, adfrealplans);
		header = header.replace(NO_ERROR_TAG, noErrorFormula);
		//header = header.replace(G_INIT_TAG, "");		
		body = body.replace(GOAL_MODULES_TAG, planModules);

		//footer = footer.replace(EVENTS_TAG, adfevents);

		String model = header + "\n" + body;
		ManageWriter.printModel(adf, model);

		//adf.println(header);
		//adf.println(body);
		//adf.println(footer);
		//adf.close();
	}

	private void printEvalBash( PrintWriter pw ){

		evalBash = evalBash.replace(PARAMS_BASH_TAG, evalFormulaParams);
		evalBash = evalBash.replace(REPLACE_BASH_TAG, evalFormulaReplace);

		ManageWriter.printModel(pw, evalBash);
	}
	
	/*Remove multiple line breaks*/
	private void cleanPlanModules() {
		String planModule = planModules.toString();
		planModules = planModules.delete(0, planModules.length()-1);		
		planModule = planModule.replaceAll("[\t\r\n][\t\r\n]+[\n]+", "\n\n");
		planModules = planModules.append(planModule);
	}

	/**
	 * Copy source dir files into target one adapting the package tags
	 * 
	 * @param input source input dir
	 * @param output target ouput dir
	 * @param utilPkgName current util package name
	 * 
	 * @throws CodeGenerationException
	 */
	/*private void copyDir( String input, String output, String utilPkgName ) throws CodeGenerationException {
		//verify input dir presence
		File inputDir = new File( input );
		if( !inputDir.exists()) {
			String msg = "Error: the input folder '" + input + "' doesn't exist. Exit";
			ATCConsole.println( msg );
			throw new CodeGenerationException( msg );
		}
		//verify output dir creation
		File outputDir = new File( output );
		if( !outputDir.exists() && !outputDir.mkdirs() ) {
			String msg = "Error: Can't create output directory \"" + outputDir + "\"!";
			ATCConsole.println( msg );
			throw new CodeGenerationException( msg );
		}
		//copy all the util file found into the input template dir
		File[] files = inputDir.listFiles();
		for( File curr : files ) { 
			if( curr.isDirectory() ) {
				//do not attemp to copy directory (avoid 'CVS' problem)
				continue;
			}
			String file = readFileAsString( curr.getAbsolutePath() );
			//update tags
			file = file.replace( UTIL_PACKAGE_TAG, utilPkgName );
			file = file.replace( CAPABILITY_AGENT_TAG, PathLocation.CAPABILITY_AGENT_NAME );
			//save it
			writeFile( file, output + curr.getName() );
		}
	}*/



	/**
	 * Write the specified content into a file
	 * 
	 * @param content the content of the file
	 * @param filename the target file
	 * 
	 * @throws CodeGenerationException 
	 */
	/*private void writeFile(String content, String filename) throws CodeGenerationException {
		try {
			FileUtility.writeFile( content, filename );
		} catch (IOException e) {
			String msg = "Error: Can't create file \"" + filename + "\"!";
			ATCConsole.println( msg );
			throw new CodeGenerationException( msg );
		}
	}*/
}