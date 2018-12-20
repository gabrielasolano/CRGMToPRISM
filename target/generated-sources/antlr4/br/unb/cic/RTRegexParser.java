// Generated from br/unb/cic/RTRegex.g4 by ANTLR 4.3
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
public class RTRegexParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, FLOAT=9, 
		SEQ=10, INT=11, C_SEQ=12, C_RTRY=13, ALT=14, TASK=15, GOAL=16, X=17, SKIPP=18, 
		NEWLINE=19, WS=20;
	public static final String[] tokenNames = {
		"<INVALID>", "'try('", "'opt('", "':'", "'?'", "'DM('", "'('", "')'", 
		"','", "FLOAT", "';'", "'#'", "'+'", "'@'", "'|'", "'T'", "'G'", "'X'", 
		"'skip'", "NEWLINE", "WS"
	};
	public static final int
		RULE_rt = 0, RULE_expr = 1, RULE_multiple = 2, RULE_id = 3;
	public static final String[] ruleNames = {
		"rt", "expr", "multiple", "id"
	};

	@Override
	public String getGrammarFileName() { return "RTRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RTRegexParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RtContext extends ParserRuleContext {
		public RtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rt; }
	 
		public RtContext() { }
		public void copyFrom(RtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BlankContext extends RtContext {
		public TerminalNode NEWLINE() { return getToken(RTRegexParser.NEWLINE, 0); }
		public BlankContext(RtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitBlank(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitBlank(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrintExprContext extends RtContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(RTRegexParser.NEWLINE, 0); }
		public PrintExprContext(RtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterPrintExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitPrintExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitPrintExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RtContext rt() throws RecognitionException {
		RtContext _localctx = new RtContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_rt);
		try {
			setState(12);
			switch (_input.LA(1)) {
			case T__7:
			case T__6:
			case T__3:
			case T__2:
			case TASK:
			case GOAL:
			case SKIPP:
				_localctx = new PrintExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(8); expr(0);
				setState(9); match(NEWLINE);
				}
				break;
			case NEWLINE:
				_localctx = new BlankContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(11); match(NEWLINE);
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
	public static class GDMContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GDMContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGDM(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGDM(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGDM(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GIdContext extends ExprContext {
		public Token t;
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public GIdContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GTryContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GTryContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGTry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGTry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGTry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GSkipContext extends ExprContext {
		public TerminalNode SKIPP() { return getToken(RTRegexParser.SKIPP, 0); }
		public GSkipContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGSkip(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGSkip(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGSkip(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GTimeContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GTimeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGTime(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GOptContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public GOptContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGOpt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGOpt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGOpt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GCardContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FLOAT() { return getToken(RTRegexParser.FLOAT, 0); }
		public GCardContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGCard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGCard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGCard(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GAltContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GAltContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGAlt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGAlt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGAlt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GDecisionMakingContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public GDecisionMakingContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterGDecisionMaking(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitGDecisionMaking(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitGDecisionMaking(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			switch (_input.LA(1)) {
			case T__6:
				{
				_localctx = new GOptContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(15); match(T__6);
				setState(16); expr(0);
				setState(17); match(T__1);
				}
				break;
			case T__7:
				{
				_localctx = new GTryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(19); match(T__7);
				setState(20); expr(0);
				setState(21); match(T__1);
				setState(22); match(T__4);
				setState(23); expr(0);
				setState(24); match(T__5);
				setState(25); expr(0);
				}
				break;
			case SKIPP:
				{
				_localctx = new GSkipContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(27); match(SKIPP);
				}
				break;
			case TASK:
			case GOAL:
				{
				_localctx = new GIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28);
				((GIdContext)_localctx).t = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TASK || _la==GOAL) ) {
					((GIdContext)_localctx).t = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(29); id();
				}
				break;
			case T__3:
				{
				_localctx = new GDecisionMakingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30); match(T__3);
				setState(31); expr(0);
				setState(32); match(T__1);
				}
				break;
			case T__2:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34); match(T__2);
				setState(35); expr(0);
				setState(36); match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(54);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(52);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new GAltContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(40);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(41); ((GAltContext)_localctx).op = match(ALT);
						setState(42); expr(10);
						}
						break;

					case 2:
						{
						_localctx = new GTimeContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(43);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(44);
						((GTimeContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SEQ || _la==INT) ) {
							((GTimeContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(45); expr(7);
						}
						break;

					case 3:
						{
						_localctx = new GDMContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(46);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(47); ((GDMContext)_localctx).op = match(T__0);
						setState(48); expr(3);
						}
						break;

					case 4:
						{
						_localctx = new GCardContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(49);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(50);
						((GCardContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << C_SEQ) | (1L << C_RTRY))) != 0)) ) {
							((GCardContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(51); match(FLOAT);
						}
						break;
					}
					} 
				}
				setState(56);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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

	public static class MultipleContext extends ParserRuleContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public MultipleContext multiple() {
			return getRuleContext(MultipleContext.class,0);
		}
		public MultipleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiple; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterMultiple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitMultiple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitMultiple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultipleContext multiple() throws RecognitionException {
		MultipleContext _localctx = new MultipleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_multiple);
		try {
			setState(62);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(57); expr(0);
				setState(58); ((MultipleContext)_localctx).op = match(T__0);
				setState(59); multiple();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(61); expr(0);
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

	public static class IdContext extends ParserRuleContext {
		public TerminalNode X() { return getToken(RTRegexParser.X, 0); }
		public TerminalNode FLOAT() { return getToken(RTRegexParser.FLOAT, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RTRegexListener ) ((RTRegexListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RTRegexVisitor ) return ((RTRegexVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_id);
		try {
			setState(68);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64); match(FLOAT);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(65); match(FLOAT);
				setState(66); match(X);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(67); match(X);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 9);

		case 1: return precpred(_ctx, 6);

		case 2: return precpred(_ctx, 2);

		case 3: return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26I\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\5\2\17\n\2\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3)\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\67\n\3"+
		"\f\3\16\3:\13\3\3\4\3\4\3\4\3\4\3\4\5\4A\n\4\3\5\3\5\3\5\3\5\5\5G\n\5"+
		"\3\5\2\3\4\6\2\4\6\b\2\5\3\2\21\22\3\2\f\r\3\2\r\17Q\2\16\3\2\2\2\4(\3"+
		"\2\2\2\6@\3\2\2\2\bF\3\2\2\2\n\13\5\4\3\2\13\f\7\25\2\2\f\17\3\2\2\2\r"+
		"\17\7\25\2\2\16\n\3\2\2\2\16\r\3\2\2\2\17\3\3\2\2\2\20\21\b\3\1\2\21\22"+
		"\7\4\2\2\22\23\5\4\3\2\23\24\7\t\2\2\24)\3\2\2\2\25\26\7\3\2\2\26\27\5"+
		"\4\3\2\27\30\7\t\2\2\30\31\7\6\2\2\31\32\5\4\3\2\32\33\7\5\2\2\33\34\5"+
		"\4\3\2\34)\3\2\2\2\35)\7\24\2\2\36\37\t\2\2\2\37)\5\b\5\2 !\7\7\2\2!\""+
		"\5\4\3\2\"#\7\t\2\2#)\3\2\2\2$%\7\b\2\2%&\5\4\3\2&\'\7\t\2\2\')\3\2\2"+
		"\2(\20\3\2\2\2(\25\3\2\2\2(\35\3\2\2\2(\36\3\2\2\2( \3\2\2\2($\3\2\2\2"+
		")8\3\2\2\2*+\f\13\2\2+,\7\20\2\2,\67\5\4\3\f-.\f\b\2\2./\t\3\2\2/\67\5"+
		"\4\3\t\60\61\f\4\2\2\61\62\7\n\2\2\62\67\5\4\3\5\63\64\f\f\2\2\64\65\t"+
		"\4\2\2\65\67\7\13\2\2\66*\3\2\2\2\66-\3\2\2\2\66\60\3\2\2\2\66\63\3\2"+
		"\2\2\67:\3\2\2\28\66\3\2\2\289\3\2\2\29\5\3\2\2\2:8\3\2\2\2;<\5\4\3\2"+
		"<=\7\n\2\2=>\5\6\4\2>A\3\2\2\2?A\5\4\3\2@;\3\2\2\2@?\3\2\2\2A\7\3\2\2"+
		"\2BG\7\13\2\2CD\7\13\2\2DG\7\23\2\2EG\7\23\2\2FB\3\2\2\2FC\3\2\2\2FE\3"+
		"\2\2\2G\t\3\2\2\2\b\16(\668@F";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}