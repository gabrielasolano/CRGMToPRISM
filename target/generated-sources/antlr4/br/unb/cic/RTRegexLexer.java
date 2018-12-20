// Generated from br/unb/cic/RTRegex.g4 by ANTLR 4.3
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
public class RTRegexLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, FLOAT=9, 
		DM=10, SEQ=11, INT=12, C_SEQ=13, C_RTRY=14, ALT=15, TASK=16, GOAL=17, 
		X=18, SKIPP=19, NEWLINE=20, WS=21;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "FLOAT", 
		"DM", "SEQ", "INT", "C_SEQ", "C_RTRY", "ALT", "TASK", "GOAL", "X", "SKIPP", 
		"NEWLINE", "WS", "DIGIT"
	};


	public RTRegexLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "RTRegex.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\27{\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\6\nI\n\n\r\n\16\nJ\3\n\5\nN\n\n\3\n\7\nQ\n\n\f\n\16\nT\13\n"+
		"\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21"+
		"\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\6\25o\n\25\r\25\16"+
		"\25p\3\26\6\26t\n\26\r\26\16\26u\3\26\3\26\3\27\3\27\2\2\30\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\2\3\2\5\4\2\f\f\17\17\3\2\13\13\3\2\62;~\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3/\3\2\2\2\5\64\3\2\2\2\7"+
		"9\3\2\2\2\t;\3\2\2\2\13=\3\2\2\2\rA\3\2\2\2\17C\3\2\2\2\21E\3\2\2\2\23"+
		"H\3\2\2\2\25U\3\2\2\2\27X\3\2\2\2\31Z\3\2\2\2\33\\\3\2\2\2\35^\3\2\2\2"+
		"\37`\3\2\2\2!b\3\2\2\2#d\3\2\2\2%f\3\2\2\2\'h\3\2\2\2)n\3\2\2\2+s\3\2"+
		"\2\2-y\3\2\2\2/\60\7v\2\2\60\61\7t\2\2\61\62\7{\2\2\62\63\7*\2\2\63\4"+
		"\3\2\2\2\64\65\7q\2\2\65\66\7r\2\2\66\67\7v\2\2\678\7*\2\28\6\3\2\2\2"+
		"9:\7<\2\2:\b\3\2\2\2;<\7A\2\2<\n\3\2\2\2=>\7F\2\2>?\7O\2\2?@\7*\2\2@\f"+
		"\3\2\2\2AB\7*\2\2B\16\3\2\2\2CD\7+\2\2D\20\3\2\2\2EF\7.\2\2F\22\3\2\2"+
		"\2GI\5-\27\2HG\3\2\2\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2KM\3\2\2\2LN\7\60"+
		"\2\2ML\3\2\2\2MN\3\2\2\2NR\3\2\2\2OQ\5-\27\2PO\3\2\2\2QT\3\2\2\2RP\3\2"+
		"\2\2RS\3\2\2\2S\24\3\2\2\2TR\3\2\2\2UV\7F\2\2VW\7O\2\2W\26\3\2\2\2XY\7"+
		"=\2\2Y\30\3\2\2\2Z[\7%\2\2[\32\3\2\2\2\\]\7-\2\2]\34\3\2\2\2^_\7B\2\2"+
		"_\36\3\2\2\2`a\7~\2\2a \3\2\2\2bc\7V\2\2c\"\3\2\2\2de\7I\2\2e$\3\2\2\2"+
		"fg\7Z\2\2g&\3\2\2\2hi\7u\2\2ij\7m\2\2jk\7k\2\2kl\7r\2\2l(\3\2\2\2mo\t"+
		"\2\2\2nm\3\2\2\2op\3\2\2\2pn\3\2\2\2pq\3\2\2\2q*\3\2\2\2rt\t\3\2\2sr\3"+
		"\2\2\2tu\3\2\2\2us\3\2\2\2uv\3\2\2\2vw\3\2\2\2wx\b\26\2\2x,\3\2\2\2yz"+
		"\t\4\2\2z.\3\2\2\2\b\2JMRpu\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}