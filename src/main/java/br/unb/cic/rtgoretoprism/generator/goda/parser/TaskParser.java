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

import br.unb.cic.ThrowingErrorListener;
import br.unb.cic.TaskRegexBaseVisitor;
import br.unb.cic.TaskRegexLexer;
import br.unb.cic.TaskRegexParser;
import br.unb.cic.TaskRegexParser.PrintExprContext;
import br.unb.cic.TaskRegexParser.TFloatContext;
import br.unb.cic.TaskRegexParser.TIntContext;
import br.unb.cic.TaskRegexParser.TNoRTContext;
import br.unb.cic.TaskRegexParser.TRTContext;
import br.unb.cic.TaskRegexParser.TTryContext;
import br.unb.cic.rtgoretoprism.model.task.TaskPattern;

public class TaskParser {
	public static Object[] parseRegex(String regex) throws IOException {
		// Reading the DSL script
		InputStream is = new ByteArrayInputStream((regex + '\n').getBytes("UTF-8"));

		// Loading the DSL script into the ANTLR stream.
		CharStream cs = new ANTLRInputStream(is);

		// Passing the input to the lexer to create tokens
		TaskRegexLexer lexer = new TaskRegexLexer(cs);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Passing the tokens to the parser to create the parse trea.
		TaskRegexParser parser = new TaskRegexParser(tokens);

		parser.removeErrorListeners();
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		ParseTree tree = parser.task();
		TaskIdParserVisitor taskRegexVisitor = new TaskIdParserVisitor();
		
		return new Object[] { taskRegexVisitor.memory, taskRegexVisitor.visit(tree) };
	}
}

class TaskIdParserVisitor extends TaskRegexBaseVisitor<String> {

	List<TaskPattern> memory = new ArrayList<TaskPattern>();

	@Override
	public String visitPrintExpr(PrintExprContext ctx) {
		return visit(ctx.expr());
	}
	
	@Override
	public String visitTRT(TRTContext ctx) {
		String id = ctx.taskid().getText();
		String desc = ctx.VAR().getText();
		String rt = ctx.RT().getText();
		memory.add(new TaskPattern(id, desc, rt));
		return id + ": " + desc + " [" + rt + "]";
	}
	
	@Override
	public String visitTNoRT(TNoRTContext ctx) {
		String id = ctx.taskid().getText();
		String desc = ctx.VAR().getText();
		memory.add(new TaskPattern(id, desc, null));
		return id + ": " + desc;
	}
	
	@Override
	public String visitTTry(TTryContext ctx) {
		String id = ctx.taskid().getText();
		String desc = ctx.VAR().getText();
		String rt = ctx.TRY().getText();
		rt = rt.substring(1, rt.length());
		memory.add(new TaskPattern(id, desc, rt));
		return id + ":" + desc + " [" + rt + "]";
	}
	
	@Override
	public String visitTFloat(TFloatContext ctx) {
		return ctx.TASKFLOAT().getText();
	}
	
	@Override
	public String visitTInt(TIntContext ctx) {
		return ctx.TASKINT().getText();
	}
}
