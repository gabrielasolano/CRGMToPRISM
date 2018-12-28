package br.unb.cic.rtgoretoprism.generator.goda.producer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unb.cic.rtgoretoprism.console.ATCConsole;
import br.unb.cic.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.rtgoretoprism.generator.goda.parser.CostParser;
import br.unb.cic.rtgoretoprism.generator.goda.parser.RTParser;
import br.unb.cic.rtgoretoprism.generator.goda.writer.ManageWriter;
import br.unb.cic.rtgoretoprism.generator.goda.writer.ParamWriter;
import br.unb.cic.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamAndGenerator;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamOrGenerator;
import br.unb.cic.rtgoretoprism.paramwrapper.ParamWrapper;
import br.unb.cic.rtgoretoprism.util.FileUtility;
import br.unb.cic.rtgoretoprism.util.PathLocation;
import it.itc.sra.taom4e.model.core.informalcore.Actor;
import it.itc.sra.taom4e.model.core.informalcore.formalcore.FHardGoal;

public class PARAMProducer {

	private String sourceFolder;
	private String targetFolder;
	private String toolsFolder;
	private Set<Actor> allActors;
	private Set<FHardGoal> allGoals;

	private String agentName;
	private List<String> leavesId = new ArrayList<String>();
	private List<String> opts_formula = new ArrayList<String>();
	private Map<String,String> ctxInformation = new HashMap<String,String>();
	private Map<String,String> reliabilityByNode = new HashMap<String,String>();

	public PARAMProducer(Set<Actor> allActors, Set<FHardGoal> allGoals, String in, String out, String tools) {

		this.sourceFolder = in;
		this.targetFolder = out;
		this.toolsFolder = tools;
		this.allActors = allActors;
		this.allGoals = allGoals;
	}

	public void run() throws CodeGenerationException, IOException {

		long startTime = new Date().getTime();

		for(Actor actor : allActors){

			RTGoreProducer producer = new RTGoreProducer(allActors, allGoals, sourceFolder, targetFolder, false);
			AgentDefinition ad = producer.run();

			agentName = ad.getAgentName();

			ATCConsole.println("Generating PARAM formulas for: " + agentName);

			// Compose goal formula
			String reliabilityForm = composeNodeForm(ad.rootlist.getFirst(), true);
			String costForm = composeNodeForm(ad.rootlist.getFirst(), false);
			
			reliabilityForm = cleanNodeForm(reliabilityForm, true);
			costForm = cleanNodeForm(costForm, false);

			//Print formula
			printFormula(reliabilityForm, costForm);

		}
		ATCConsole.println( "PARAM formulas created in " + (new Date().getTime() - startTime) + "ms.");
	}

	private String cleanNodeForm(String nodeForm, boolean reliability) {
		
		if (!reliability) {
			nodeForm = replaceReliabilites(nodeForm);
		}
		
		nodeForm = cleanMultipleContexts(nodeForm);
		
		nodeForm = nodeForm.replaceAll("\\s+", "");
		nodeForm = nodeForm.replaceAll("\\+1", " +1");
		nodeForm = nodeForm.replaceAll("-1", " -1");
		nodeForm = nodeForm.replaceAll("\\+(?!1)", " + ");
		nodeForm = nodeForm.replaceAll("-(?!1)", " - ");
		
		return nodeForm;
	}

	private String cleanMultipleContexts(String nodeForm) {
		
		String[] plusSignalSplit = nodeForm.split("\\+");
		
		for (String exp1 : plusSignalSplit) {
			String[] minusSignalSplit = exp1.split("-");
			for (String exp2 : minusSignalSplit) {
				String aux = exp2.replaceAll("\\(","");
				aux = aux.replaceAll("\\)","");
				aux = aux.trim();
				if (!aux.equals("1")) {
					String[] multSignalSplit = exp2.split("\\*");
					nodeForm = replaceCtxRepetition(nodeForm, multSignalSplit);
				}
			}
		}
		
		return nodeForm;
	}

	private String replaceCtxRepetition(String nodeForm, String[] multSignalSplit) {
		Set<String> lump = new HashSet<String>();
		String withoutRepetition = new String();
		String withRepetition = new String();
		boolean isZero = false;
		 
		for (String i : multSignalSplit) {
			
			if (i.contains("/")) {
				i = i.substring(0, i.indexOf("/")-1);
			}
			
			i = i.replaceAll("\\(", "");
			i = i.replaceAll("\\)", "");

			if (withRepetition.isEmpty()) withRepetition = i;
			else withRepetition = withRepetition + "\\*" + i;

			i = i.trim();

			if (!lump.contains(i)) {
				lump.add(i);
				if (!i.equals("1")) {
					if (withoutRepetition.isEmpty()) withoutRepetition = i;
					else withoutRepetition = withoutRepetition + "*" + i;
				}
				if (i.equals("0")) isZero = true;
		    }
		}
		
		if (isZero) {
			withRepetition = "\\+" + withRepetition;
			nodeForm = nodeForm.replaceAll(withRepetition, "");
			withRepetition = withRepetition.substring(1,withRepetition.length());
			withRepetition = "-" + withRepetition;
			nodeForm = nodeForm.replaceAll(withRepetition, "");
			return nodeForm;
		}
		nodeForm = nodeForm.replaceAll(withRepetition, withoutRepetition);
		return nodeForm;
	}

	private String replaceReliabilites(String nodeForm) {
		if (nodeForm.contains(" R_")) {
			String[] variables = nodeForm.split(" ");
			for (String var : variables) {
				if (var.contains("R_") && !var.contains("XOR")) {
					String id = var.substring(2, var.length());
					String reliability = this.reliabilityByNode.get(id);
					nodeForm = nodeForm.replaceAll(" " + var + " ", " " + reliability + " ");
				}
			}
		}
		return nodeForm;
	}

	/*private void generatePctlFormula() throws IOException {

		StringBuilder pctl = new StringBuilder("P=? [ true U (");
		StringBuilder goals = new StringBuilder();
		int i = 0;

		for(FHardGoal goal : allGoals){
			pctl.append(AgentDefinition.parseElId(goal.getName()) + (i < allGoals.size() - 1 ? "&" : ""));
			goals.append(AgentDefinition.parseElId(goal.getName()));
			i++;
		}

		pctl.append(") ]");

		FileUtility.deleteFile(targetFolder + "/AgentRole_" + agentName + "/reachability.pctl", false);
		FileUtility.writeFile(pctl.toString(), targetFolder + "/AgentRole_" + agentName + "/reachability.pctl");
	}*/

	private void printFormula(String reliabilityForm, String costForm) throws CodeGenerationException {

		reliabilityForm = composeFormula(reliabilityForm);
		costForm = composeFormula(costForm);

		String output = targetFolder + "/" + PathLocation.BASIC_AGENT_PACKAGE_PREFIX + agentName + "/";

		PrintWriter reliabiltyFormula = ManageWriter.createFile("reliability.out", output);
		PrintWriter costFormula = ManageWriter.createFile("cost.out", output);
		
		ManageWriter.printModel(reliabiltyFormula, reliabilityForm);
		ManageWriter.printModel(costFormula, costForm);
	}

	private String composeFormula(String nodeForm) throws CodeGenerationException {

		String paramInputFolder = sourceFolder + "/PARAM/";

		/*String body = ManageWriter.readFileAsString(paramInputFolder + "formulabody.param");

		for (String opt : this.opts_formula) {

			body = body + opt + ", ";
		}

		for (String leaf : this.leavesId) {

			body = body + "rTask" + leaf + (leaf.equals(leavesId.get(leavesId.size()-1))? "]\n[" : ", ");
		}

		for (String opt : this.opts_formula) {

			body = body + "[0, 1] ";
		}

		for (String leaf : this.leavesId) {
			body = body + "[0, 1]" + (leaf.equals(leavesId.get(leavesId.size()-1))? "]\n" : " ");
		}

		body = body + "  " + nodeForm + "\n\n"; */

		String body = nodeForm + "\n\n";
		for (String ctxKey : ctxInformation.keySet()) {

			body = body + "//" + ctxKey + " = " + ctxInformation.get(ctxKey) + "\n";
		}

		return body;
	}

	//true: compose reliability, false: compose cost
	private String composeNodeForm(RTContainer rootNode, boolean reliability) throws IOException, CodeGenerationException {

		Const decType;
		String rtAnnot;
		String nodeForm;
		String nodeId;
		List<String> ctxAnnot = new ArrayList<String>();
		LinkedList<GoalContainer> decompGoal = new LinkedList<GoalContainer>();
		LinkedList<PlanContainer> decompPlans = new LinkedList<PlanContainer>();

		if(rootNode instanceof GoalContainer) nodeId = rootNode.getClearUId();
		else nodeId = rootNode.getClearElId();

		decompGoal = rootNode.getDecompGoals();
		decompPlans = rootNode.getDecompPlans();
		decType = rootNode.getDecomposition();
		rtAnnot = rootNode.getRtRegex();
		ctxAnnot = rootNode.getFulfillmentConditions();

		//nodeForm = getNodeForm(decType, rtAnnot, nodeId, reliability);
		nodeForm = getNodeForm(decType, rtAnnot, nodeId, reliability, rootNode);
		
		/*Run for sub goals*/
		for (GoalContainer subNode : decompGoal) {
			String subNodeId = subNode.getClearUId();
			String subNodeForm = composeNodeForm(subNode, reliability);
			nodeForm = replaceSubForm(nodeForm, subNodeForm, nodeId, subNodeId);
		}

		/*Run for sub tasks*/
		for (PlanContainer subNode : decompPlans) {
			String subNodeId = subNode.getClearElId();
			String subNodeForm = composeNodeForm(subNode, reliability);
			nodeForm = replaceSubForm(nodeForm, subNodeForm, nodeId, subNodeId);
		}

		/*If leaf task*/
		if ((decompGoal.size() == 0) && (decompPlans.size() == 0)) {

			this.leavesId.add(nodeId);

			if (reliability) {
				//Create DTMC model (param)
				ParamWriter writer = new ParamWriter(sourceFolder, nodeId);
				String model = writer.writeModel();

				//Call to param (reliability)
				ParamWrapper paramWrapper = new ParamWrapper(toolsFolder, nodeId);
				nodeForm = paramWrapper.getFormula(model);
			}
			else {
				//Cost
				nodeForm = getCostFormula(rootNode);
			}

			if (!ctxAnnot.isEmpty() && !nodeForm.equals("0")) {
				nodeForm = insertCtxAnnotation(nodeForm, ctxAnnot, nodeId);
				if (rootNode.isAlternative())
					removeXorOpt(nodeId, "XOR");
				if (rootNode.isOptional())
					removeXorOpt(nodeId, "OPT");
			}	
		}
		if (reliability) this.reliabilityByNode.put(nodeId, nodeForm);

		return nodeForm;
	}

	private String getCostFormula(RTContainer rootNode) throws IOException {
		PlanContainer plan = (PlanContainer) rootNode;
	
		if (plan.getCostRegex() != null) {
			Object [] res = CostParser.parseRegex(plan.getCostRegex());
			return (String) res[2];
		}
		
		return "0";
	}

	/*Remove XOR or OPT parameter from the symbolic formula*/
	private void removeXorOpt(String nodeId, String xorOpt) {
		List<String> opt_formula_aux = new ArrayList<String>();
		for (String xor : this.opts_formula) {
			String id = xor.replaceAll(xorOpt + "_", "");
			if (!nodeId.equals(id)) {
				opt_formula_aux.add(xor);
			}
		}
		this.opts_formula = opt_formula_aux;
	}

	private String insertCtxAnnotation(String nodeForm, List<String> ctxAnnot, String nodeId) {

		List<String> cleanCtx = clearCtxList(ctxAnnot);

		String ctxParamId = "CTX_" + nodeId;
		nodeForm = ctxParamId + "*" + nodeForm;

		String ctxConcat = new String();
		for (String ctx : cleanCtx) {
			if (ctxConcat.length() == 0) {
				ctxConcat = "(" + ctx + ")";
			}
			else {
				ctxConcat = ctxConcat.concat(" & (" + ctx + ")");
			}
		}

		ctxInformation.put(ctxParamId, ctxConcat);

		if (!this.opts_formula.contains(ctxParamId)) {
			this.opts_formula.add(ctxParamId);
		}

		return nodeForm;
	}

	private List<String> clearCtxList(List<String> ctxAnnot) {

		List<String> clearCtx = new ArrayList<String>();
		for (String ctx : ctxAnnot) {
			String[] aux;
			if (ctx.contains("assertion condition")) {
				aux = ctx.split("^assertion condition\\s*");
			}
			else {
				aux = ctx.split("^assertion trigger\\s*");
			}
			clearCtx.add(aux[1]);
		}

		return clearCtx;
	}

	private String replaceSubForm(String nodeForm, String subNodeForm, String nodeId, String subNodeId) {

		if (nodeForm.equals(nodeId)) {
			nodeForm = subNodeForm;
		}
		else {
			subNodeId = restricToString(subNodeId);
			subNodeForm = restricToString(subNodeForm);
			nodeForm = nodeForm.replaceAll(subNodeId, subNodeForm);
		}
		
		if (subNodeForm.contains("CTX") && nodeForm.contains("XOR")) {
			for (Map.Entry<String, String> entry : ctxInformation.entrySet())
			{
				if (entry.getKey().contains(subNodeId.trim())) {
					nodeForm = nodeForm.replaceAll("XOR_" + subNodeId.trim(), entry.getKey());
					return nodeForm;
				}
			}
		}
		return nodeForm;
	}

	private String restricToString(String subNodeString) {
		return " " + subNodeString + " ";
	}

	/*private String getNodeForm(Const decType, String rtAnnot, String uid, boolean reliability) throws IOException {
		
		if (rtAnnot == null) {
			return uid;
		}

		Object [] res = RTParser.parseRegex(uid, rtAnnot + '\n', decType, true);

		checkOptXorDeclaration((String) res[5]);

		if (reliability) return (String) res[5];		
		return (String) res[6];
	}*/
	
	//Get node form for AND/OR-decompositions and DM annotation only
	private String getNodeForm(Const decType, String rtAnnot, String uid, boolean reliability, RTContainer rootNode) throws IOException {
		
		if (rtAnnot == null) {
			List<String> childrenNodes = getChildrenId(rootNode);
	
			if (childrenNodes.size() <= 1) return uid;
		
			String formula = new String();
			if (decType.equals(Const.AND)) {
				if (reliability) {
					formula = "( ";
					for (String id : childrenNodes) {
						formula += id + " * ";
					}
					formula = formula.substring(0, formula.length()-2);
					formula += " )";
				}
				else {
					SymbolicParamAndGenerator param = new SymbolicParamAndGenerator();
					formula = param.getSequentialAndCost((String[]) childrenNodes.toArray(new String[0]));
				}
			}
			else {
				SymbolicParamOrGenerator param = new SymbolicParamOrGenerator();
				if (reliability) {
					formula = param.getSequentialOrReliability((String[]) childrenNodes.toArray(new String[0]));
				}
				else {
					formula = param.getSequentialOrCost((String[]) childrenNodes.toArray(new String[0]));
				}
			}
			return formula;
		}

		Object [] res = RTParser.parseRegex(uid, rtAnnot + '\n', decType, true);

		checkOptXorDeclaration((String) res[5]);

		if (reliability) return (String) res[5];		
		return (String) res[6];
	}

	private List<String> getChildrenId(RTContainer rootNode) {
		List<String> ids = new ArrayList<String>();
		LinkedList<RTContainer> children = rootNode.getDecompElements();
		
		if (children.isEmpty()) return ids;
		
		for (RTContainer child : children) {
			if(child instanceof GoalContainer) ids.add(child.getClearUId());
			else ids.add(child.getClearElId());
		}
		
		return ids;
	}

	private void checkOptXorDeclaration(String formula) {

		if (formula.contains("OPT")) {
			String regex = "OPT_(.*?) ";
			Matcher match = Pattern.compile(regex).matcher(formula);
			while (match.find()) {
				String optDeclaration = "OPT_" + match.group(1);
				if (!this.opts_formula.contains(optDeclaration)) {
					this.opts_formula.add(optDeclaration);
				}
			}
		}

		if (formula.contains("XOR")) {
			String regex = "XOR_(.*?) ";
			Matcher match = Pattern.compile(regex).matcher(formula);
			while (match.find()) {
				String optDeclaration = "XOR_" + match.group(1);
				if (!this.opts_formula.contains(optDeclaration)) {
					this.opts_formula.add(optDeclaration);
				}
			}
		}
	}
}