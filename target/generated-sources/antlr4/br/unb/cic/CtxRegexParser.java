// Generated from br/unb/cic/CtxRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CtxRegexParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, VAR=11, FLOAT=12, BOOL=13, NEWLINE=14, WS=15, LETTER=16;
	public static final String[] tokenNames = {
		"<INVALID>", "'<='", "'&'", "'assertion condition '", "'!='", "'>='", 
		"'|'", "'<'", "'='", "'assertion trigger '", "'>'", "VAR", "FLOAT", "BOOL", 
		"NEWLINE", "WS", "LETTER"
	};
	public static final int
		RULE_ctx = 0, RULE_expr = 1, RULE_value = 2;
	public static final String[] ruleNames = {
		"ctx", "expr", "value"
	};

	@Override
	public String getGrammarFileName() { return "CtxRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CtxRegexParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CtxContext extends ParserRuleContext {
		public CtxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ctx; }
	 
		public CtxContext() { }
		public void copyFrom(CtxContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConditionContext extends CtxContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ConditionContext(CtxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlankContext extends CtxContext {
		public TerminalNode NEWLINE() { return getToken(CtxRegexParser.NEWLINE, 0); }
		public BlankContext(CtxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitBlank(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitBlank(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TriggerContext extends CtxContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TriggerContext(CtxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterTrigger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitTrigger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitTrigger(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrintExprContext extends CtxContext {
		public CtxContext ctx() {
			return getRuleContext(CtxContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(CtxRegexParser.NEWLINE, 0); }
		public PrintExprContext(CtxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterPrintExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitPrintExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitPrintExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CtxContext ctx() throws RecognitionException {
		return ctx(0);
	}

	private CtxContext ctx(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CtxContext _localctx = new CtxContext(_ctx, _parentState);
		CtxContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_ctx, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			switch (_input.LA(1)) {
			case T__7:
				{
				_localctx = new ConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(7); match(T__7);
				setState(8); expr();
				}
				break;
			case T__1:
				{
				_localctx = new TriggerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(9); match(T__1);
				setState(10); expr();
				}
				break;
			case NEWLINE:
				{
				_localctx = new BlankContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(11); match(NEWLINE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(18);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrintExprContext(new CtxContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_ctx);
					setState(14);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(15); match(NEWLINE);
					}
					} 
				}
				setState(20);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CGEContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CGEContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCGE(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCGE(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCGE(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CGTContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CGTContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCGT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCGT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCGT(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class COrContext extends ExprContext {
		public Token op;
		public List<TerminalNode> VAR() { return getTokens(CtxRegexParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(CtxRegexParser.VAR, i);
		}
		public COrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CLTContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CLTContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCLT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCLT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCLT(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CAndContext extends ExprContext {
		public Token op;
		public List<TerminalNode> VAR() { return getTokens(CtxRegexParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(CtxRegexParser.VAR, i);
		}
		public CAndContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CVarContext extends ExprContext {
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CVarContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCVar(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CDIFFContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CDIFFContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCDIFF(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCDIFF(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCDIFF(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CLEContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CLEContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCLE(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCLE(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCLE(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CEQContext extends ExprContext {
		public Token op;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode VAR() { return getToken(CtxRegexParser.VAR, 0); }
		public CEQContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCEQ(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCEQ(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCEQ(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expr);
		try {
			setState(46);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new CLTContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(21); match(VAR);
				setState(22); ((CLTContext)_localctx).op = match(T__3);
				setState(23); value();
				}
				break;

			case 2:
				_localctx = new CLEContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(24); match(VAR);
				setState(25); ((CLEContext)_localctx).op = match(T__9);
				setState(26); value();
				}
				break;

			case 3:
				_localctx = new CGTContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(27); match(VAR);
				setState(28); ((CGTContext)_localctx).op = match(T__0);
				setState(29); value();
				}
				break;

			case 4:
				_localctx = new CGEContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(30); match(VAR);
				setState(31); ((CGEContext)_localctx).op = match(T__5);
				setState(32); value();
				}
				break;

			case 5:
				_localctx = new CEQContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(33); match(VAR);
				setState(34); ((CEQContext)_localctx).op = match(T__2);
				setState(35); value();
				}
				break;

			case 6:
				_localctx = new CDIFFContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(36); match(VAR);
				setState(37); ((CDIFFContext)_localctx).op = match(T__6);
				setState(38); value();
				}
				break;

			case 7:
				_localctx = new CAndContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(39); match(VAR);
				setState(40); ((CAndContext)_localctx).op = match(T__8);
				setState(41); match(VAR);
				}
				break;

			case 8:
				_localctx = new COrContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(42); match(VAR);
				setState(43); ((COrContext)_localctx).op = match(T__4);
				setState(44); match(VAR);
				}
				break;

			case 9:
				_localctx = new CVarContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(45); match(VAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
	 
		public ValueContext() { }
		public void copyFrom(ValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CFloatContext extends ValueContext {
		public TerminalNode FLOAT() { return getToken(CtxRegexParser.FLOAT, 0); }
		public CFloatContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCFloat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCFloat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CBoolContext extends ValueContext {
		public TerminalNode BOOL() { return getToken(CtxRegexParser.BOOL, 0); }
		public CBoolContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).enterCBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CtxRegexListener ) ((CtxRegexListener)listener).exitCBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CtxRegexVisitor ) return ((CtxRegexVisitor<? extends T>)visitor).visitCBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_value);
		try {
			setState(50);
			switch (_input.LA(1)) {
			case FLOAT:
				_localctx = new CFloatContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(48); match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new CBoolContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(49); match(BOOL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0: return ctx_sempred((CtxContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean ctx_sempred(CtxContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\22\67\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\2\3\2\3\2\3\2\5\2\17\n\2\3\2\3\2\7\2\23\n\2\f\2"+
		"\16\2\26\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\61\n\3\3\4\3\4\5\4\65"+
		"\n\4\3\4\2\3\2\5\2\4\6\2\2?\2\16\3\2\2\2\4\60\3\2\2\2\6\64\3\2\2\2\b\t"+
		"\b\2\1\2\t\n\7\5\2\2\n\17\5\4\3\2\13\f\7\13\2\2\f\17\5\4\3\2\r\17\7\20"+
		"\2\2\16\b\3\2\2\2\16\13\3\2\2\2\16\r\3\2\2\2\17\24\3\2\2\2\20\21\f\6\2"+
		"\2\21\23\7\20\2\2\22\20\3\2\2\2\23\26\3\2\2\2\24\22\3\2\2\2\24\25\3\2"+
		"\2\2\25\3\3\2\2\2\26\24\3\2\2\2\27\30\7\r\2\2\30\31\7\t\2\2\31\61\5\6"+
		"\4\2\32\33\7\r\2\2\33\34\7\3\2\2\34\61\5\6\4\2\35\36\7\r\2\2\36\37\7\f"+
		"\2\2\37\61\5\6\4\2 !\7\r\2\2!\"\7\7\2\2\"\61\5\6\4\2#$\7\r\2\2$%\7\n\2"+
		"\2%\61\5\6\4\2&\'\7\r\2\2\'(\7\6\2\2(\61\5\6\4\2)*\7\r\2\2*+\7\4\2\2+"+
		"\61\7\r\2\2,-\7\r\2\2-.\7\b\2\2.\61\7\r\2\2/\61\7\r\2\2\60\27\3\2\2\2"+
		"\60\32\3\2\2\2\60\35\3\2\2\2\60 \3\2\2\2\60#\3\2\2\2\60&\3\2\2\2\60)\3"+
		"\2\2\2\60,\3\2\2\2\60/\3\2\2\2\61\5\3\2\2\2\62\65\7\16\2\2\63\65\7\17"+
		"\2\2\64\62\3\2\2\2\64\63\3\2\2\2\65\7\3\2\2\2\6\16\24\60\64";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}