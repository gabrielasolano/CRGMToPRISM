package br.unb.cic.rtgoretoprism.generator.goda.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import br.unb.cic.CtxRegexBaseVisitor;
import br.unb.cic.CtxRegexLexer;
import br.unb.cic.CtxRegexParser;
import br.unb.cic.CtxRegexParser.CAndContext;
import br.unb.cic.CtxRegexParser.CBoolContext;
import br.unb.cic.CtxRegexParser.CDIFFContext;
import br.unb.cic.CtxRegexParser.CEQContext;
import br.unb.cic.CtxRegexParser.CFloatContext;
import br.unb.cic.CtxRegexParser.CGEContext;
import br.unb.cic.CtxRegexParser.CGTContext;
import br.unb.cic.CtxRegexParser.CLEContext;
import br.unb.cic.CtxRegexParser.CLTContext;
import br.unb.cic.CtxRegexParser.COrContext;
import br.unb.cic.CtxRegexParser.CVarContext;
import br.unb.cic.CtxRegexParser.ConditionContext;
import br.unb.cic.CtxRegexParser.PrintExprContext;
import br.unb.cic.CtxRegexParser.TriggerContext;
import br.unb.cic.ThrowingErrorListener;
import br.unb.cic.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.rtgoretoprism.model.ctx.CtxSymbols;

public class CtxParser{
	
	public static void main (String [] args){
		try {
			System.out.println(CtxParser.parseRegex("assertion condition MEMORY<30&PROCESSOR<=80\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object[] parseRegex(String regex) throws IOException{
		//Reading the DSL script
	    InputStream is = new ByteArrayInputStream((regex + '\n').getBytes("UTF-8"));
	    
	    //Loading the DSL script into the ANTLR stream.
	    CharStream cs = new ANTLRInputStream(is);
	    
	    //Passing the input to the lexer to create tokens
	    CtxRegexLexer lexer = new CtxRegexLexer(cs);
	    lexer.removeErrorListeners();
	    lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
	    
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    
	    //Passing the tokens to the parser to create the parse trea. 
	    CtxRegexParser parser = new CtxRegexParser(tokens);
	    parser.removeErrorListeners();
	    parser.addErrorListener(ThrowingErrorListener.INSTANCE);
	    //Semantic model to be populated
	    //Graph g = new Graph();
	    
	    //Adding the listener to facilitate walking through parse tree. 
	    //parser.addParseListener(new MyCtxRegexBaseListener());
	    
	    //invoking the parser. 
	    //parser.prog();
	    
	    //Graph.printGraph(g);
	    
	    //ParseTreeWalker walker = new ParseTreeWalker();
	    //walker.walk(new MyCtxRegexBaseListener(), parser.prog());
	    
	    ParseTree tree = parser.ctx();
	    CtxFormulaParserVisitor CtxRegexVisitor = new CtxFormulaParserVisitor();
	    //rtRegexVisitor.visit(tree);
	    
	    
	    return new Object[]{CtxRegexVisitor.memory, CtxRegexVisitor.visit(tree), CtxRegexVisitor.type};
	    
	    //return new Object [] 	{CtxRegexVisitor.memory};
	}
}

class CtxFormulaParserVisitor extends  CtxRegexBaseVisitor<String> {

	Set<String[]> ctxVars = new HashSet<String[]>();
	List<ContextCondition> memory = new ArrayList<ContextCondition>();
	CtxSymbols type = null;
	
	@Override
	public String visitPrintExpr(PrintExprContext ctx) {
		return visit(ctx.ctx());		
	}
	
	@Override
	public String visitCondition(ConditionContext ctx) {
		type = CtxSymbols.COND;
		return visit(ctx.expr());
	}
	
	@Override
	public String visitTrigger(TriggerContext ctx) {
		type = CtxSymbols.TRIG;
		return visit(ctx.expr());
	}
	
	@Override
	public String visitCVar(CVarContext ctx) {
		String var = ctx.VAR().getText();
		if(ctx.getParent() instanceof CAndContext ||
		   ctx.getParent() instanceof COrContext ||
		   ctx.getParent() instanceof ConditionContext ||
		   ctx.getParent() instanceof TriggerContext){
			ctxVars.add(new String[]{var, "bool"});
			memory.add(new ContextCondition(var, CtxSymbols.BOOL,  "true"));
		}else
			ctxVars.add(new String[]{var, "double"});
		return var;
	}	
	
	@Override
	public String visitCEQ(CEQContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.EQ,  value));
		return var + " = " + value;
	}
	
	@Override
	public String visitCDIFF(CDIFFContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.DIFF,  value));
		return var + " != " + value;
	}
	
	@Override
	public String visitCLE(CLEContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.LE,  value));
		return var + " <= " + value;
	}
	
	@Override
	public String visitCLT(CLTContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.LT,  value));
		return var + " < " + value;
	}
	
	@Override
	public String visitCGE(CGEContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.GE,  value));
		return var + " >= " + value;
	}
	
	@Override
	public String visitCGT(CGTContext ctx) {
		String var = ctx.VAR().getText();
		String value = visit(ctx.value());
		//String var = visit(ctx.expr(0));
		//String value = visit(ctx.expr(1));
		memory.add(new ContextCondition(var, CtxSymbols.GT,  value));
		return var + " > " + value;
	}
	
	@Override
	public String visitCAnd(CAndContext ctx) {
		String varA = ctx.VAR(0).getText();
		String varB = ctx.VAR(1).getText();
		//String varA = visit(ctx.expr(0));
		//String varB = visit(ctx.expr(1));
		return varA + " & " + varB;
	}
	
	@Override
	public String visitCOr(COrContext ctx) {
		String varA = ctx.VAR(0).getText();
		String varB = ctx.VAR(1).getText();
		//String varA = visit(ctx.expr(0));
		//String varB = visit(ctx.expr(1));
		return varA + " | " + varB;
	}
	
	@Override
	public String visitCBool(CBoolContext ctx) {
		return ctx.BOOL().getText();
	}
	
	@Override
	public String visitCFloat(CFloatContext ctx) {		
		return ctx.FLOAT().getText();
	}
	
	private CtxSymbols getEffectType(ParserRuleContext ctx){
		if(ctx instanceof ConditionContext)
			return CtxSymbols.COND;
		else// if(ctx instanceof TriggerContext)
			return CtxSymbols.TRIG;
	}
	
	private CtxSymbols parseSymbol(String op){
		for(CtxSymbols symbol : CtxSymbols.values())
			if(symbol.toString().equals(op))
				return symbol;
		return null;
	}
}