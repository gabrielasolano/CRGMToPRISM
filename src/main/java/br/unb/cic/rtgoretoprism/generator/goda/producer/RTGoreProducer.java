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

package br.unb.cic.rtgoretoprism.generator.goda.producer;

import it.itc.sra.taom4e.model.core.gencore.TroposModelElement;
import it.itc.sra.taom4e.model.core.informalcore.Actor;
import it.itc.sra.taom4e.model.core.informalcore.Dependency;
import it.itc.sra.taom4e.model.core.informalcore.Plan;
import it.itc.sra.taom4e.model.core.informalcore.TroposIntentional;
import it.itc.sra.taom4e.model.core.informalcore.formalcore.FContribution;
import it.itc.sra.taom4e.model.core.informalcore.formalcore.FHardGoal;
import it.itc.sra.taom4e.model.core.informalcore.formalcore.FPlan;
import it.itc.sra.taom4e.model.core.informalcore.formalcore.FSoftGoal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.unb.cic.rtgoretoprism.console.ATCConsole;
import br.unb.cic.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.rtgoretoprism.generator.goda.parser.CostParser;
import br.unb.cic.rtgoretoprism.generator.goda.parser.RTParser;
import br.unb.cic.rtgoretoprism.generator.goda.writer.PrismWriter;
import br.unb.cic.rtgoretoprism.generator.goda.writer.dtmc.DTMCWriter;
import br.unb.cic.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.model.kl.ElementContainer;
import br.unb.cic.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.rtgoretoprism.model.kl.SoftgoalContainer;
import br.unb.cic.rtgoretoprism.util.FileUtility;
import br.unb.cic.rtgoretoprism.util.NameUtility;
import br.unb.cic.rtgoretoprism.util.kl.TroposNavigator;

/**
 * 
 * 
 * @author Mirko Morandini
 *
 */
public class RTGoreProducer {
	/** the navigator for the Tropos model */
	private TroposNavigator tn;

	/** input folder for the templates */ 
	private String inputFolder;
	/** output folder for the generated code */
	private String outputFolder;
	/** the set of Actor for which code should be generated */
	private Set<Actor> allActors;
	/** the set of Actor for which code should be generated */
	private Set<FHardGoal> allGoals;
	/** defines if the generated code will be parametric (PARAM) or not (PRISM) **/
	private boolean parametric;

	/** memory for the parsed RT regexes */
	List<String> rtDMGoals;

	/**
	 * Creates a new Producer instance
	 * 
	 * @param allActors the actor to generate code for
	 * @param in the template input folder
	 * @param out the generated code output folder
	 */
	public RTGoreProducer(Set<Actor> allActors, Set<FHardGoal> allGoals, String in, String out, boolean parametric ) {

		tn = new TroposNavigator( allActors.iterator().next().eResource() );

		this.inputFolder = in;
		this.outputFolder = out;
		this.allActors = allActors;
		this.allGoals = allGoals;

		this.rtDMGoals = new ArrayList<String>();
		this.parametric = parametric;
	}

	/**
	 * Run the process by which the Jadex source code is generated for the
	 * specified Tropos (system) actors
	 * 
	 * @throws CodeGenerationException 
	 * @throws IOException 
	 */
	public AgentDefinition run() throws CodeGenerationException, IOException {
		ATCConsole.println("Starting PRISM Model Generation Process (Knowledge Level)" );
		ATCConsole.println("\tTemplate Input Folder: " + inputFolder );
		ATCConsole.println("\tOutput Folder: " + outputFolder );

		long startTime = new Date().getTime();
		AgentDefinition ad = null;

		for( Actor a : allActors ) {
			if(!a.isIsSystem())
				return ad;

			ATCConsole.println( "Generating MDP model for: " + a.getName() );

			//generate the AgentDefinition object for the current actor
			ad = new AgentDefinition( a );

			// analyse all root goals
			for( FHardGoal rootgoal : tn.getRootGoals(a) ) {
				Const type, request;

				if( tn.isDelegated(rootgoal) ) {
					type = Const.ACHIEVE;
					request=Const.REQUEST;
				} else {
					// type=Const.MAINTAIN;					
					//TODO:  For now only achieve is implemented, this has to be a maintain-goal!
					type = Const.ACHIEVE; 
					request = Const.NONE;
				}

				//create the goalcontainer for this one
				GoalContainer gc = ad.createGoal(rootgoal, type);
				gc.setRequest(request);

				//add to the root goal list
				ad.addRootGoal(gc);
				addGoal(rootgoal, gc, ad, false);

			}		
			//TODO: check this planlist creation, maybe it can be added on the fly
			//the list of root plans for current agent' capabilities
			List<Plan> planList = tn.getAllCapabilityPlan( a );

			// write the DTMC PRISM file to the output folder given the template input folder
			PrismWriter writer = new DTMCWriter( ad, planList, inputFolder, outputFolder, parametric);
			writer.writeModel();

			//Generate pctl formulas
			generatePctlFormulas(ad);
		}
		ATCConsole.println( "MDP model created in " + (new Date().getTime() - startTime) + "ms.");
		return ad;
	}

	private void generatePctlFormulas(AgentDefinition ad) throws IOException {

		StringBuilder pmax = new StringBuilder("Pmax=? [ F \"success\" ]");
		StringBuilder pmin = new StringBuilder("Pmin=? [ F \"success\" ]");
		StringBuilder rmax = new StringBuilder("R{\"cost\"}max=? [ F \"success\" ]");
		StringBuilder rmin = new StringBuilder("R{\"cost\"}min=? [ F \"success\" ]");

		FileUtility.deleteFile(outputFolder + "/AgentRole_" + ad.getAgentName() + "/ReachabilityMax.pctl", false);
		FileUtility.deleteFile(outputFolder + "/AgentRole_" + ad.getAgentName() + "/ReachabilityMin.pctl", false);
		FileUtility.deleteFile(outputFolder + "/AgentRole_" + ad.getAgentName() + "/CostMax.pctl", false);
		FileUtility.deleteFile(outputFolder + "/AgentRole_" + ad.getAgentName() + "/CostMin.pctl", false);

		FileUtility.writeFile(pmax.toString(), outputFolder + "/AgentRole_" + ad.getAgentName() + "/ReachabilityMax.pctl");
		FileUtility.writeFile(pmin.toString(), outputFolder + "/AgentRole_" + ad.getAgentName() + "/ReachabilityMin.pctl");
		FileUtility.writeFile(rmax.toString(), outputFolder + "/AgentRole_" + ad.getAgentName() + "/CostMax.pctl");
		FileUtility.writeFile(rmin.toString(), outputFolder + "/AgentRole_" + ad.getAgentName() + "/CostMin.pctl");
	}

	/**
	 * 
	 * @param g
	 * @param gc
	 * @param ad
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void addGoal(FHardGoal g, GoalContainer gc, final AgentDefinition ad, boolean included) throws IOException {

		included = included || allGoals.isEmpty() || allGoals.contains(g);
		gc.setIncluded(included);
		addContributions(g, gc, ad);

		//TODO: uncomment if needed!
		//if (tn.isDelegated(g))
		//for testing purposes we allow requests for all goals
		//gc.setRequest(Const.REQUEST);

		String rtRegex = gc.getRtRegex();
		boolean dmRT = false;
		dmRT = storeRegexResults(gc.getUid(), rtRegex, gc.getDecomposition());

		List<FHardGoal> declist = (List<FHardGoal>) tn.getBooleanDec(g, FHardGoal.class);
		sortIntentionalElements(declist);
		if (tn.isBooleanDecAND(g))
			// sets decomposition flag and creates the AND-Plan (call only one time!)
			gc.createDecomposition(Const.AND);			
		else if (tn.isBooleanDecOR(g))
			// sets decomposition flag and creates the Metagoal+plan (call only one time!)
			gc.createDecomposition(Const.OR);	

		if (dmRT) gc.setDecisionMaking(this.rtDMGoals);

		iterateGoals(ad, gc, declist, included);
		iterateMeansEnds(g, gc, ad, included);
		//Set goals alternatives, tries, optional and cardinalities


		if (tn.isGoalWhyDependency(g)) {
			for (Dependency dep : tn.getGoalDependencies(g)) {
				//String goal = AgentDefinition.fill(tn.getDependumGoalFromDependency(dep).getName());
				String goal = NameUtility.adjustName( tn.getDependumGoalFromDependency(dep).getName() );
				//String actor = AgentDefinition.fill(tn.getActorFromDependency(dep).getName());
				String actor = NameUtility.adjustName( tn.getActorFromDependency(dep).getName() );
				gc.addDependency(goal, actor);
			}
		}

		if (gc.isDecisionMaking()) {
			storeDecisionMakingNodes(gc);
		}

		if (gc.getClearElId().contains("X")) {
			String fulfillment = "assertion condition " + gc.getClearElId() + " = true";
			gc.addFulfillmentConditions(fulfillment);
			PlanContainer unknownPlan = new PlanContainer(gc, fulfillment);
			unknownPlan.setElId("TX");
			unknownPlan.setTimePath(gc.getTimePath());
			unknownPlan.setTimeSlot(gc.getTimeSlot());
			unknownPlan.setPrevTimePath(gc.getPrevTimePath());
			unknownPlan.setFutTimePath(gc.getFutTimePath());
			gc.addMERealPlan(unknownPlan);
		}
	}

	private void iterateGoals(AgentDefinition ad, GoalContainer gc, List<FHardGoal> decList, boolean include) throws IOException{

		Integer prevPath = gc.getPrevTimePath();
		Integer rootFutPath = gc.getFutTimePath();
		Integer rootPath = gc.getTimePath();
		Integer rootTime = gc.getTimeSlot();
		gc.setRootTimeSlot(rootTime);

		for (FHardGoal dec : decList) {
			boolean newgoal = !ad.containsGoal(dec);

			boolean first = false;
			if (gc.getDecompGoals().isEmpty()) first = true;

			GoalContainer deccont = ad.createGoal(dec, Const.ACHIEVE);
			gc.addDecomp(deccont);

			if (this.rtDMGoals.contains(deccont.getElId())) {
				deccont.setPrevTimePath(gc.getPrevTimePath()+1);
				deccont.setFutTimePath(gc.getFutTimePath()+1);
				deccont.setTimePath(rootPath+1);
				deccont.setTimeSlot(deccont.getPrevTimePath() + 1);	

				if (!first) deccont.setFutTimePath(rootPath+1);
			}
			else {
				if (!first) {
					deccont.setPrevTimePath(gc.getFutTimePath());
					deccont.setFutTimePath(gc.getFutTimePath()+1);
					deccont.setTimePath(rootPath);
					deccont.setTimeSlot(deccont.getPrevTimePath() + 1);
				}
				else{ 
					deccont.setPrevTimePath(prevPath);
					deccont.setFutTimePath(rootFutPath);
					deccont.setTimePath(rootPath);
					deccont.setTimeSlot(gc.getPrevTimePath()+1);
				}
			}
			deccont.addFulfillmentConditions(gc.getFulfillmentConditions());
			if (newgoal){
				addGoal(dec, deccont, ad, include);	
				gc.setFutTimePath(Math.max(deccont.getTimeSlot(), deccont.getFutTimePath()));		
			}	
		}		
	}


	/**
	 * @param g
	 * @param gc
	 * @param ad
	 */
	private void addContributions(TroposModelElement m, ElementContainer ec, AgentDefinition ad) {
		if (tn.hasContributions(m)) {
			for (FContribution c : tn.getContributions(m)) {

				if (c.getTarget() instanceof FSoftGoal) {
					FSoftGoal sg = (FSoftGoal) c.getTarget();
					SoftgoalContainer sgcont = ad.createSoftgoal(sg);
					ec.addContribution(sgcont, c.getMetric());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addPlan(Plan p, PlanContainer pc, final AgentDefinition ad) throws IOException {
		addContributions(p, pc, ad);

		boolean dmRT = false;
		if (tn.isBooleanDec(p)){
			dmRT = storeRegexResults(pc.getUid(), pc.getRtRegex(), pc.getDecomposition());
		}
		else {
			pc.setCostRegex(pc.getRtRegex());
			pc.setRtRegex(null);
			storeCostResults(pc);
		}

		if (dmRT) pc.setDecisionMaking(this.rtDMGoals);

		if (tn.isMeansEndDec(p)){
			List<FPlan> melist = tn.getMeansEndMeanPlans(p);
			sortIntentionalElements(melist);
			// sets decomposition flag and creates the Metagoal+plan,
			// shall be the same than with OR! They could also be mixed in this implementation!
			pc.createDecomposition(Const.ME);
			iteratePlans(ad, pc, melist);			
		}
		else {			
			List<FPlan> decList = (List<FPlan>) tn.getBooleanDec(p, FPlan.class);
			sortIntentionalElements(decList);
			// sets decomposition flag and creates the Metagoal+plan,
			// shall be the same than with OR! They could also be mixed in this implementation!
			if (tn.isBooleanDecAND(p)) 
				pc.createDecomposition(Const.AND);
			else
				pc.createDecomposition(Const.OR);
			iteratePlans(ad, pc, decList);			
		}

		if (pc.isDecisionMaking()) {
			storeDecisionMakingNodes(pc);
		}

		if (pc.getClearElId().contains("X")) pc.addFulfillmentConditions("assertion trigger " + pc.getClearElId() + " = true");
	}

	private void storeDecisionMakingNodes(RTContainer pc) {

		List<String> idNodes = pc.getDecisionMaking();
		LinkedList<RTContainer> nodes = new LinkedList<RTContainer>();

		for (String id : idNodes) {
			nodes.add(pc.getDecompElement(id));
		}
		pc.setDecisionNodes(nodes);
	}

	private void iteratePlans(AgentDefinition ad, PlanContainer pc, List<FPlan> decList) throws IOException{

		Integer prevPath = pc.getPrevTimePath();
		Integer rootFutPath = pc.getFutTimePath();
		Integer rootPath = pc.getTimePath();
		for (FPlan dec : decList) {
			boolean newplan = !ad.containsPlan(dec);

			boolean first = false;
			if (pc.getDecompPlans().isEmpty()) first = true;

			PlanContainer deccont = ad.createPlan(dec);
			pc.addDecomp(deccont);

			if (this.rtDMGoals.contains(deccont.getElId())) {
				deccont.setPrevTimePath(pc.getPrevTimePath()+1);
				deccont.setFutTimePath(pc.getFutTimePath()+1);
				deccont.setTimePath(rootPath+1);
				deccont.setTimeSlot(deccont.getPrevTimePath() + 1);	

				if (!first) deccont.setFutTimePath(rootPath+1);

			}
			else { 
				if (!first) {
					deccont.setPrevTimePath(pc.getFutTimePath());
					deccont.setFutTimePath(pc.getFutTimePath()+1);
					deccont.setTimePath(rootPath);
					deccont.setTimeSlot(deccont.getPrevTimePath() + 1);
				}else{
					deccont.setPrevTimePath(prevPath);
					deccont.setFutTimePath(rootFutPath);
					deccont.setTimePath(rootPath);
					deccont.setTimeSlot(prevPath+1);
				}
			}
			deccont.addFulfillmentConditions(pc.getFulfillmentConditions());

			if (newplan){
				addPlan(dec, deccont, ad);									
				pc.setFutTimePath(Math.max(deccont.getTimeSlot(), deccont.getFutTimePath())); //TODO: why to add pc.getFutTimePath() ?
			}
		}
	}

	private void iterateMeansEnds(FHardGoal g, GoalContainer gc, final AgentDefinition ad, boolean included) throws IOException{

		if (included && tn.isMeansEndDec(g)){
			List<FPlan> melist = tn.getMeansEndMeanPlans(g);
			sortIntentionalElements(melist);

			gc.createDecomposition(Const.ME);
			for (FPlan p : melist) {
				boolean newplan = !ad.containsPlan(p);

				PlanContainer pc = ad.createPlan(p);
				gc.addMERealPlan(pc);

				pc.setPrevTimePath(gc.getPrevTimePath());
				pc.setFutTimePath(gc.getFutTimePath());
				pc.setTimePath(gc.getTimePath());
				pc.setTimeSlot(gc.getPrevTimePath()+1);

				pc.addFulfillmentConditions(gc.getFulfillmentConditions());

				if (newplan){
					addPlan(p, pc, ad);					
					gc.setFutTimePath(Math.max(pc.getTimeSlot(), pc.getFutTimePath()));
				}
			}			

			//The unusual "means-end" with a goal as means:
			//The "means" goal is afterwards handled like in an OR-decomposition!
			List<FHardGoal> megoallist = tn.getMeansEndMeanGoals(g);

			// sets decomposition flag and creates the Metagoal+plan,
			// shall be the same than with OR! They could also be mixed in this implementation!
			// gc.createDecomposition(Const.ME);
			for (FHardGoal go : megoallist) {
				boolean newgoal = !ad.containsGoal(go);

				GoalContainer pc = ad.createGoal(go, Const.ACHIEVE);
				//gc.addMERealPlan(pc);
				gc.addDecomp(pc);
				if (newgoal)
					addGoal(go, pc, ad, true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean storeRegexResults(String uid, String rtRegex, Const decType) throws IOException {
		if(rtRegex != null){
			Object [] res = RTParser.parseRegex(uid, rtRegex + '\n', decType, false);
			rtDMGoals.addAll((List<String>) res [2]);

			List<String> dmList = (List<String>) res[2];
			if (!dmList.isEmpty()) return true;
		}
		return false;
	}

	private void storeCostResults(PlanContainer pc) throws IOException {
		if (pc.getCostRegex() != null) {
			Object [] res = CostParser.parseRegex(pc.getCostRegex());
			pc.setCostValue((String) res[0]);
			pc.setCostVariable((String) res[1]);
		}
	}

	private void sortIntentionalElements(List<? extends TroposIntentional> elements){

		Collections.sort(elements, new Comparator<TroposIntentional>() {
			@Override
			public int compare(TroposIntentional gA, TroposIntentional gB) {
				String idA = AgentDefinition.parseElId(gA.getName()).replaceAll("[TG]", "");
				String idB = AgentDefinition.parseElId(gB.getName()).replaceAll("[TG]", "");
				return idA.compareToIgnoreCase(idB);
			}
		});
	}

	/*private Set<Actor> getSystemActors(){
		List<Actor> actors = tn.getActors();
		Set<Actor> systemActors = new HashSet<Actor>();
		for(Actor a : actors){
			if(a.isIsSystem())
				systemActors.add(a);
		}
		return systemActors;
	}*/
}
