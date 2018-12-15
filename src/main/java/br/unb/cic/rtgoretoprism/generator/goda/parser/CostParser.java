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
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import br.unb.cic.CostRegexBaseVisitor;
import br.unb.cic.CostRegexLexer;
import br.unb.cic.CostRegexParser;
import br.unb.cic.CostRegexParser.GCostContext;
import br.unb.cic.CostRegexParser.GVariableContext;
import br.unb.cic.RTRegexBaseVisitor;
import br.unb.cic.RTRegexLexer;
import br.unb.cic.RTRegexParser;
import br.unb.cic.RTRegexParser.GAltContext;
import br.unb.cic.RTRegexParser.GCardContext;
import br.unb.cic.RTRegexParser.GIdContext;
import br.unb.cic.RTRegexParser.GOptContext;
import br.unb.cic.RTRegexParser.GSkipContext;
import br.unb.cic.RTRegexParser.GTimeContext;
import br.unb.cic.RTRegexParser.GTryContext;
import br.unb.cic.RTRegexParser.ParensContext;
import br.unb.cic.RTRegexParser.PrintExprContext;
import br.unb.cic.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.rtgoretoprism.model.ctx.CtxSymbols;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.paramformula.SymbolicParamAltGenerator;



public class CostParser{

	public static Object[] parseRegex(String regex) throws IOException{
		//Reading the DSL script
		InputStream is = new ByteArrayInputStream(regex.getBytes("UTF-8"));

		//Loading the DSL script into the ANTLR stream.
		CharStream cs = new ANTLRInputStream(is);

		//Passing the input to the lexer to create tokens
		CostRegexLexer lexer = new CostRegexLexer(cs);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		//Passing the tokens to the parser to create the parse trea. 
		CostRegexParser parser = new CostRegexParser(tokens);
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

		ParseTree tree = parser.cost();
		CostRegexVisitor costRegexVisitor = new CostRegexVisitor();
		costRegexVisitor.visit(tree);

		return new Object [] {costRegexVisitor.costValue, costRegexVisitor.costVariable};
	}
}

class CostRegexVisitor extends  CostRegexBaseVisitor<String> {
	String costValue = null;
	String costVariable = null;
	
	@Override
	public String visitGCost(GCostContext ctx) {
		costValue = ctx.FLOAT().getText();
		return costValue;
	}

	@Override
	public String visitGVariable(GVariableContext ctx) {
		costValue = ctx.FLOAT().getText();
		costVariable = ctx.VAR().getText();

		return costValue + "*" + costVariable;
	}

}