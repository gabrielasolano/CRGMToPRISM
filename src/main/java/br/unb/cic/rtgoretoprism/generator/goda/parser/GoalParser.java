package br.unb.cic.rtgoretoprism.generator.goda.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import br.unb.cic.GoalRegexBaseVisitor;
import br.unb.cic.GoalRegexLexer;
import br.unb.cic.GoalRegexParser;
import br.unb.cic.GoalRegexParser.GNoRTContext;
import br.unb.cic.GoalRegexParser.GRTContext;
import br.unb.cic.GoalRegexParser.GTryContext;
import br.unb.cic.GoalRegexParser.PrintExprContext;
import br.unb.cic.ThrowingErrorListener;
import br.unb.cic.rtgoretoprism.model.goal.GoalPattern;

public class GoalParser {
	public static Object[] parseRegex(String regex) throws IOException {
		// Reading the DSL script
		InputStream is = new ByteArrayInputStream((regex + '\n').getBytes("UTF-8"));

		// Loading the DSL script into the ANTLR stream.
		CharStream cs = new ANTLRInputStream(is);

		// Passing the input to the lexer to create tokens
		GoalRegexLexer lexer = new GoalRegexLexer(cs);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Passing the tokens to the parser to create the parse trea.
		GoalRegexParser parser = new GoalRegexParser(tokens);

		parser.removeErrorListeners();
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		ParseTree tree = parser.goal();
		GoalIdParserVisitor goalRegexVisitor = new GoalIdParserVisitor();
		
		return new Object[] { goalRegexVisitor.memory, goalRegexVisitor.visit(tree) };
	}
}

class GoalIdParserVisitor extends GoalRegexBaseVisitor<String> {

	List<GoalPattern> memory = new ArrayList<GoalPattern>();

	@Override
	public String visitPrintExpr(PrintExprContext ctx) {
		return visit(ctx.expr());
	}
	
	@Override
	public String visitGTry(GTryContext ctx) {
		String id = ctx.GOALID().getText();
		String desc = ctx.VAR().getText();
		String rt = ctx.TRY().getText();
		rt = rt.substring(1, rt.length());
		memory.add(new GoalPattern(id, desc, rt));
		return id + ":" + desc + " [" + rt + "]";
	}
	
	@Override
	public String visitGRT(GRTContext ctx) {
		String id = ctx.GOALID().getText();
		String desc = ctx.VAR().getText();
		String rt = ctx.RT().getText();
		memory.add(new GoalPattern(id, desc, rt));
		return id + ": " + desc + " [" + rt + "]";
	}
	
	@Override
	public String visitGNoRT(GNoRTContext ctx) {
		String id = ctx.GOALID().getText();
		String desc = ctx.VAR().getText();
		memory.add(new GoalPattern(id, desc, null));
		return id + ": " + desc;
	}
}
