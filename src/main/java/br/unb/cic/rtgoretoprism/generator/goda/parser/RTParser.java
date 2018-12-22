package br.unb.cic.rtgoretoprism.generator.goda.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import br.unb.cic.RTRegexBaseVisitor;
import br.unb.cic.RTRegexLexer;
import br.unb.cic.RTRegexParser;
import br.unb.cic.RTRegexParser.GAltContext;
import br.unb.cic.RTRegexParser.GCardContext;
import br.unb.cic.RTRegexParser.GDMContext;
import br.unb.cic.RTRegexParser.GDecisionMakingContext;
import br.unb.cic.RTRegexParser.GIdContext;
import br.unb.cic.RTRegexParser.GOptContext;
import br.unb.cic.RTRegexParser.GSkipContext;
import br.unb.cic.RTRegexParser.GTimeContext;
import br.unb.cic.RTRegexParser.GTryContext;
import br.unb.cic.RTRegexParser.MultipleContext;
import br.unb.cic.RTRegexParser.ParensContext;
import br.unb.cic.RTRegexParser.PrintExprContext;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamAltGenerator;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamAndGenerator;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamOrGenerator;



public class RTParser{

	public static Object[] parseRegex(String uid, String regex, Const decType, boolean param) throws IOException{
		//Reading the DSL script
		InputStream is = new ByteArrayInputStream(regex.getBytes("UTF-8"));

		//Loading the DSL script into the ANTLR stream.
		CharStream cs = new ANTLRInputStream(is);

		//Passing the input to the lexer to create tokens
		RTRegexLexer lexer = new RTRegexLexer(cs);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//Passing the tokens to the parser to create the parse trea. 
		RTRegexParser parser = new RTRegexParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		//Semantic model to be populated
		//Graph g = new Graph();

		//Adding the listener to facilitate walking through parse tree. 
		//parser.addParseListener(new MyRTRegexBaseListener());

		//invoking the parser. 
		//parser.prog();

		//Graph.printGraph(g);

		//ParseTreeWalker walker = new ParseTreeWalker();
		//walker.walk(new MyRTRegexBaseListener(), parser.prog());

		ParseTree tree = parser.rt();
		CustomRTRegexVisitor rtRegexVisitor = new CustomRTRegexVisitor(uid, decType, param);
		rtRegexVisitor.visit(tree);

		return new Object [] 	{rtRegexVisitor.timeMemory, 
				rtRegexVisitor.cardMemory, 
				rtRegexVisitor.altMemory,
				rtRegexVisitor.tryMemory,
				rtRegexVisitor.optMemory,
				rtRegexVisitor.reliabilityFormula,
				rtRegexVisitor.costFormula,
				rtRegexVisitor.decisionMemory};
	}
}

class CustomRTRegexVisitor extends  RTRegexBaseVisitor<String> {

	final String uid;
	final Const decType;
	final boolean param;
	String reliabilityFormula = new String();
	String costFormula = new String();
	Map<String, Boolean[]> timeMemory = new HashMap<String, Boolean[]>();		
	Map<String, Object[]> cardMemory = new HashMap<String, Object[]>();
	Map<String, Set<String>> altMemory = new HashMap<String, Set<String>>();
	Map<String, String[]> tryMemory = new HashMap<String, String[]>();
	Map<String, Boolean> optMemory = new HashMap<String, Boolean>();
	List<String> decisionMemory = new ArrayList<String>();

	public CustomRTRegexVisitor(String uid, Const decType, boolean param) {
		this.decType = decType;
		this.param = param;

		if (uid.contains("_")) {
			this.uid = uid.substring(0, uid.indexOf('_'));
		}
		else {
			this.uid = uid;
		}
	}

	@Override
	public String visitPrintExpr(PrintExprContext ctx) {
		visit(ctx.expr());		
		return "Goals sorted";
	}
	
	@Override
	public String visitGId(GIdContext ctx) {
		String gid = ctx.t.getText() + ctx.id().getText();
		if(ctx.t.getType() == RTRegexParser.TASK) {
			//gid = gid.replaceAll("\\.", "_");
			gid = uid + '_' + gid;
		}

		if ( !timeMemory.containsKey(gid) ){
			timeMemory.put(gid, new Boolean[]{false,false});			
			//cardMemory.put(gid, 1);
		}
		return gid;
	}

	private String checkNestedRT (String paramFormulaAux, boolean isReliability) {

		if (isReliability) {
			if (!reliabilityFormula.isEmpty()) {
				paramFormulaAux = reliabilityFormula;
				reliabilityFormula = "";
			}	
		}
		else {
			if (!costFormula.isEmpty()) {
				paramFormulaAux = costFormula;
				costFormula = "";
			}
		}
		return paramFormulaAux;
	}

	@Override
	public String visitGTime(GTimeContext ctx) {
		String gidAo = visit(ctx.expr(0));
		String paramFormulaAo = gidAo.replaceAll("\\.", "_");
		String paramCostAo = paramFormulaAo;
		paramFormulaAo = checkNestedRT(paramFormulaAo, true);
		paramCostAo = checkNestedRT(paramCostAo, false);
		
		String gidBo = visit(ctx.expr(1));
		String paramFormulaBo = gidBo.replaceAll("\\.", "_");
		String paramCostBo = paramFormulaBo;
		paramFormulaBo = checkNestedRT(paramFormulaBo, true);
		paramCostBo = checkNestedRT(paramCostBo, false);

		String [] gidAs = gidAo.split("-");
		String [] gidBs = gidBo.split("-");
		String [] gids = new String[gidAs.length + gidBs.length];
		int i = 0;
		for(String gidB : gidBs){
			Boolean [] pathTimeB = timeMemory.get(gidB);			
			if(ctx.op.getType() == RTRegexParser.INT){
				pathTimeB[0] = true;
			}else if(ctx.op.getType() == RTRegexParser.SEQ)
				pathTimeB[1] = true;
			for(String gidA : gidAs){
				gids[i] = gidA.replaceAll("\\.", "_");
				i++;
			}
			gids[i] = gidB.replaceAll("\\.", "_");
			i++; 
		}

		if (param) {
			if (decType.equals(Const.AND)) {
				reliabilityFormula = "( " + paramFormulaAo + " * " + paramFormulaBo + " )";
				SymbolicParamAndGenerator param = new SymbolicParamAndGenerator();
				if (ctx.op.getType() == RTRegexParser.SEQ) {
					costFormula = param.getSequentialAndCost(gids);
				}
				else {
					costFormula = param.getParallelAndCost(gids);
				}
			}
			else if (decType.equals(Const.OR)) {
				//paramFormula = "(MAX( " + paramFormulaAo + " , " + paramFormulaBo + " ))";
				reliabilityFormula = "( -1 * ( " + paramFormulaAo + " * " + paramFormulaBo + " ) + "
						+ paramFormulaAo + " + " + paramFormulaBo + " )";
				SymbolicParamOrGenerator param = new SymbolicParamOrGenerator();
				if (ctx.op.getType() == RTRegexParser.INT) {
					costFormula = "( " + paramCostAo + " + " + paramCostBo + " )";
				}
				else {
					costFormula = param.getSequentialOrCost(gids);
				}
			}
		}
		return gidAo + '-' + gidBo;
	}

	@Override
	public String visitGAlt(GAltContext ctx) {

		String gidAo = visit(ctx.expr(0));	
		String gidBo = visit(ctx.expr(1));

		String [] gidAs = gidAo.split("-");
		String [] gidBs = gidBo.split("-");		
		String [] gids = new String[gidAs.length + gidBs.length];

		int i = 0;
		for(String gidB : gidBs){
			for(String gidA : gidAs){
				if(ctx.op.getType() == RTRegexParser.ALT){
					addToAltSet(gidA, gidB);
					//addToAltSet(gidB, gidA);
				}
				gids[i] = gidA.replaceAll("\\.", "_");
				i++;
			}
			gids[i] = gidB.replaceAll("\\.", "_");
			i++;
		}

		if (param) {
			SymbolicParamAltGenerator param = new SymbolicParamAltGenerator();
			reliabilityFormula = param.getAlternativeFormula(gids, true);
			costFormula = param.getAlternativeFormula(gids, false);
		}
		return gidAo + '-' + gidBo;
	}

	private void addToAltSet(String gid1, String gid2){
		if(altMemory.get(gid1) == null)
			altMemory.put(gid1, new HashSet<String>());
		altMemory.get(gid1).add(gid2);
	}

	@Override
	public String visitGOpt(GOptContext ctx) {
		String gId = super.visit(ctx.expr());
		String paramFormulaId = gId.replaceAll("\\.", "_");
		String paramCostId = paramFormulaId;
		paramFormulaId = checkNestedRT(paramFormulaId, true);
		paramCostId = checkNestedRT(paramCostId, false);

		if (param) {
			String clearId = gId.replaceAll("\\.", "_");
			reliabilityFormula = "( OPT_" + clearId + " * " + paramFormulaId
					+ " - " + "OPT_" + clearId + " + 1 )"; 
			costFormula = "( OPT_" + clearId + " * R_" + clearId + " * " + paramCostId + " )";
		}
		optMemory.put(gId, true);

		return gId;
	}

	@Override
	public String visitGDM(GDMContext ctx) {
		String gidAo = super.visit(ctx.expr(0));
		String gidBo = super.visit(ctx.expr(1));
		
		String paramFormulaAo = gidAo.replaceAll("\\.", "_");
		String paramCostAo = paramFormulaAo;
		paramFormulaAo = checkNestedRT(paramFormulaAo, true);
		paramCostAo = checkNestedRT(paramCostAo, false);
		
		String paramFormulaBo = gidBo.replaceAll("\\.", "_");
		String paramCostBo = paramFormulaBo;
		paramFormulaBo = checkNestedRT(paramFormulaBo, true);
		paramCostBo = checkNestedRT(paramCostBo, false);
		
		String [] gidAs = gidAo.split("-");
		String [] gidBs = gidBo.split("-");		
		String [] gids = new String[gidAs.length + gidBs.length];
		int i = 0;
		for(String gidB : gidBs){
			for(String gidA : gidAs){
				if(!decisionMemory.contains(gidA))
					decisionMemory.add(gidA);
				gids[i] = gidA.replaceAll("\\.", "_");
				i++;
			}
			if(!decisionMemory.contains(gidB))
				decisionMemory.add(gidB);
			gids[i] = gidB.replaceAll("\\.", "_");
			i++;
		}
		
		if (param) {
			reliabilityFormula = "( -1 * ( " + paramFormulaAo + " * " + paramFormulaBo + " ) + "
					+ paramFormulaAo + " + " + paramFormulaBo + " )";
			
			SymbolicParamOrGenerator param = new SymbolicParamOrGenerator();
			if (gids.length <= 4) costFormula = param.getDecisionMakingFormula(gids);
			else costFormula = "( " + paramCostAo + " * " + paramCostBo + " )";
			 
		}
		return gidAo + '-' + gidBo;
	}


	@Override
	public String visitGCard(GCardContext ctx) {		

		String gid = visit(ctx.expr());
		String paramFormulaId = gid.replaceAll("\\.", "_");
		String paramCostId = paramFormulaId;
		paramFormulaId = checkNestedRT(paramFormulaId, true);
		paramCostId = checkNestedRT(paramCostId, true);
		
		String k = ctx.FLOAT().getText();
		if(ctx.op.getType() == RTRegexParser.INT) {
			cardMemory.put(gid, new Object[]{Const.INT,Integer.parseInt(ctx.FLOAT().getText())});
			if (param) {
				reliabilityFormula = "(( " + paramFormulaId + " )^" + k + ")";
				costFormula = "( " + getMultiplier(k) + " * ( R_" + paramCostId + " )^" + k + " * " + paramCostId + " )";
			}
		}
		else if(ctx.op.getType() == RTRegexParser.C_SEQ) {
			cardMemory.put(gid, new Object[]{Const.SEQ,Integer.parseInt(ctx.FLOAT().getText())});
			if (param) {
				reliabilityFormula = "(( " + paramFormulaId + " )^" + k + ")";
				costFormula = "( " + k + " * ( R_" + paramCostId + " )^" + k + " * " + paramCostId + " )";
			}
		}
		else {
			cardMemory.put(gid, new Object[]{Const.RTRY,Integer.parseInt(ctx.FLOAT().getText())});
			if (param) {
				k = String.valueOf(Integer.valueOf(k) + 1);
				reliabilityFormula = "( 1 - (1 - " + paramFormulaId + " )^" + k + " )";
			}
		}	
		return gid;
	}

	private String getMultiplier(String k) {
		int num = 0;
		
		for (int i = 1; i <= Integer.parseInt(k); i++) {
			num = num + i;
		}
		return Integer.toString(num);
	}

	@Override
	public String visitGTry(GTryContext ctx) {

		String gidT = visit(ctx.expr(0));
		String paramFormulaT = gidT.replaceAll("\\.", "_");
		String paramCostT = paramFormulaT;
		paramFormulaT = checkNestedRT(paramFormulaT, true);
		paramCostT = checkNestedRT(paramCostT, false);

		String gidS = visit(ctx.expr(1));
		String paramFormulaS = "1";
		String paramCostS = "1";
		paramFormulaS = checkNestedRT(paramFormulaS, true);
		paramCostS = checkNestedRT(paramCostS, false);

		String gidF = visit(ctx.expr(2));
		String paramFormulaF = "1";
		String paramCostF = "1";
		paramFormulaF = checkNestedRT(paramFormulaF, true);
		paramCostF = checkNestedRT(paramCostF, false);
 
		if(gidS != null){
			paramFormulaS = gidS.replaceAll("\\.", "_");
		}

		if(gidF != null){
			paramFormulaF = gidF.replaceAll("\\.", "_");
		}
		tryMemory.put(gidT, new String[]{gidS, gidF});
		
		if (param) {
			reliabilityFormula = "( " + paramFormulaT + " * " + paramFormulaS
					+ " - " + paramFormulaT + " * " + paramFormulaF
					+ " + " + paramFormulaF + " )";
			
			if (paramCostS.equals("1")) { //Try (n)? skip : n1
				costFormula = "( - R_" + paramCostT + " * R_" + paramCostF + " * " + paramCostT
						+ " - R_" + paramCostT + " * R_" + paramCostF + " * " + paramCostF
						+ " + R_" + paramCostT + " * " + paramCostT
						+ " + R_" + paramCostF + " * " + paramCostT
						+ " + R_" + paramCostF + " * " + paramCostF + " )";

			}
			else if (paramCostF.equals("1")) { //Try (n) ? n1 : skip
				costFormula = "( R_" + paramCostT + " * R_" + paramCostS + " * " + paramCostT
						+ " + R_" + paramCostT + " * R_" + paramCostS + " * " + paramCostS
						+ " - R_" + paramCostT + " * " + paramCostT
						+ " + " + paramCostT + " )";
			}
			else{ //Try (n)? n1 : n2
				costFormula = "( R_" + paramCostT + " * R_" + paramCostS + " * " + paramCostT
						+ " + R_" + paramCostT + " * R_" + paramCostS + " * " + paramCostS
						+ " - R_" + paramCostT + " * R_" + paramCostF + " * " + paramCostT
						+ " - R_" + paramCostT + " * R_" + paramCostF + " * " + paramCostF
						+ " + R_" + paramCostF + " * " + paramCostT
						+ " + R_" + paramCostF + " * " + paramCostF + " )";
			}
		}
		return gidT;
	}
	
	@Override
	public String visitGSkip(GSkipContext ctx) {		
		return null;
	}

	@Override
	public String visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}

}