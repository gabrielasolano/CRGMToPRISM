// Generated from br/unb/cic/CtxRegex.g4 by ANTLR 4.3
package br.unb.cic;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CtxRegexLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, VAR=11, FLOAT=12, BOOL=13, NEWLINE=14, WS=15, LETTER=16;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'"
	};
	public static final String[] ruleNames = {
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "VAR", "FLOAT", "BOOL", "NEWLINE", "WS", "DIGIT", "LETTER"
	};


	public CtxRegexLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CtxRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\22\u00a6\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\6\fb\n\f\r\f\16\fc\3\f\7\fg\n\f"+
		"\f\f\16\fj\13\f\3\f\7\fm\n\f\f\f\16\fp\13\f\3\r\6\rs\n\r\r\r\16\rt\3\r"+
		"\5\rx\n\r\3\r\7\r{\n\r\f\r\16\r~\13\r\3\16\7\16\u0081\n\16\f\16\16\16"+
		"\u0084\13\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u008f\n"+
		"\16\3\16\7\16\u0092\n\16\f\16\16\16\u0095\13\16\3\17\6\17\u0098\n\17\r"+
		"\17\16\17\u0099\3\20\6\20\u009d\n\20\r\20\16\20\u009e\3\20\3\20\3\21\3"+
		"\21\3\22\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\2#\22\3\2\6\4\2\f\f\17\17\4\2\13\13\"\"\3"+
		"\2\62;\5\2C\\aac|\u00af\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2#\3\2\2\2\3%\3\2\2\2\5(\3\2\2\2\7*\3\2\2\2\t?\3\2\2\2\13B\3\2\2"+
		"\2\rE\3\2\2\2\17G\3\2\2\2\21I\3\2\2\2\23K\3\2\2\2\25^\3\2\2\2\27a\3\2"+
		"\2\2\31r\3\2\2\2\33\u0082\3\2\2\2\35\u0097\3\2\2\2\37\u009c\3\2\2\2!\u00a2"+
		"\3\2\2\2#\u00a4\3\2\2\2%&\7>\2\2&\'\7?\2\2\'\4\3\2\2\2()\7(\2\2)\6\3\2"+
		"\2\2*+\7c\2\2+,\7u\2\2,-\7u\2\2-.\7g\2\2./\7t\2\2/\60\7v\2\2\60\61\7k"+
		"\2\2\61\62\7q\2\2\62\63\7p\2\2\63\64\7\"\2\2\64\65\7e\2\2\65\66\7q\2\2"+
		"\66\67\7p\2\2\678\7f\2\289\7k\2\29:\7v\2\2:;\7k\2\2;<\7q\2\2<=\7p\2\2"+
		"=>\7\"\2\2>\b\3\2\2\2?@\7#\2\2@A\7?\2\2A\n\3\2\2\2BC\7@\2\2CD\7?\2\2D"+
		"\f\3\2\2\2EF\7~\2\2F\16\3\2\2\2GH\7>\2\2H\20\3\2\2\2IJ\7?\2\2J\22\3\2"+
		"\2\2KL\7c\2\2LM\7u\2\2MN\7u\2\2NO\7g\2\2OP\7t\2\2PQ\7v\2\2QR\7k\2\2RS"+
		"\7q\2\2ST\7p\2\2TU\7\"\2\2UV\7v\2\2VW\7t\2\2WX\7k\2\2XY\7i\2\2YZ\7i\2"+
		"\2Z[\7g\2\2[\\\7t\2\2\\]\7\"\2\2]\24\3\2\2\2^_\7@\2\2_\26\3\2\2\2`b\5"+
		"#\22\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2dh\3\2\2\2eg\5!\21\2fe\3"+
		"\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2in\3\2\2\2jh\3\2\2\2km\5\37\20\2l"+
		"k\3\2\2\2mp\3\2\2\2nl\3\2\2\2no\3\2\2\2o\30\3\2\2\2pn\3\2\2\2qs\5!\21"+
		"\2rq\3\2\2\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2uw\3\2\2\2vx\7\60\2\2wv\3\2"+
		"\2\2wx\3\2\2\2x|\3\2\2\2y{\5!\21\2zy\3\2\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2"+
		"\2\2}\32\3\2\2\2~|\3\2\2\2\177\u0081\5\37\20\2\u0080\177\3\2\2\2\u0081"+
		"\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u008e\3\2"+
		"\2\2\u0084\u0082\3\2\2\2\u0085\u0086\7h\2\2\u0086\u0087\7c\2\2\u0087\u0088"+
		"\7n\2\2\u0088\u0089\7u\2\2\u0089\u008f\7g\2\2\u008a\u008b\7v\2\2\u008b"+
		"\u008c\7t\2\2\u008c\u008d\7w\2\2\u008d\u008f\7g\2\2\u008e\u0085\3\2\2"+
		"\2\u008e\u008a\3\2\2\2\u008f\u0093\3\2\2\2\u0090\u0092\5\37\20\2\u0091"+
		"\u0090\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2"+
		"\2\2\u0094\34\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0098\t\2\2\2\u0097\u0096"+
		"\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a"+
		"\36\3\2\2\2\u009b\u009d\t\3\2\2\u009c\u009b\3\2\2\2\u009d\u009e\3\2\2"+
		"\2\u009e\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1"+
		"\b\20\2\2\u00a1 \3\2\2\2\u00a2\u00a3\t\4\2\2\u00a3\"\3\2\2\2\u00a4\u00a5"+
		"\t\5\2\2\u00a5$\3\2\2\2\16\2chntw|\u0082\u008e\u0093\u0099\u009e\3\b\2"+
		"\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}