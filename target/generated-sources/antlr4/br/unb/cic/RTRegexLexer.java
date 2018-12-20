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
		SEQ=10, INT=11, C_SEQ=12, C_RTRY=13, ALT=14, TASK=15, GOAL=16, X=17, SKIPP=18, 
		NEWLINE=19, WS=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "FLOAT", 
		"SEQ", "INT", "C_SEQ", "C_RTRY", "ALT", "TASK", "GOAL", "X", "SKIPP", 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26v\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\6\nG\n\n\r\n\16\nH\3\n\5\nL\n\n\3\n\7\nO\n\n\f\n\16\nR\13\n\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\6\24j\n\24\r\24\16\24k\3\25\6\25o\n\25\r\25"+
		"\16\25p\3\25\3\25\3\26\3\26\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\2\3\2\5"+
		"\4\2\f\f\17\17\3\2\13\13\3\2\62;y\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\3-\3\2\2\2\5\62\3\2\2\2\7\67\3\2\2\2\t9\3\2\2\2\13;\3\2\2\2\r?\3\2"+
		"\2\2\17A\3\2\2\2\21C\3\2\2\2\23F\3\2\2\2\25S\3\2\2\2\27U\3\2\2\2\31W\3"+
		"\2\2\2\33Y\3\2\2\2\35[\3\2\2\2\37]\3\2\2\2!_\3\2\2\2#a\3\2\2\2%c\3\2\2"+
		"\2\'i\3\2\2\2)n\3\2\2\2+t\3\2\2\2-.\7v\2\2./\7t\2\2/\60\7{\2\2\60\61\7"+
		"*\2\2\61\4\3\2\2\2\62\63\7q\2\2\63\64\7r\2\2\64\65\7v\2\2\65\66\7*\2\2"+
		"\66\6\3\2\2\2\678\7<\2\28\b\3\2\2\29:\7A\2\2:\n\3\2\2\2;<\7F\2\2<=\7O"+
		"\2\2=>\7*\2\2>\f\3\2\2\2?@\7*\2\2@\16\3\2\2\2AB\7+\2\2B\20\3\2\2\2CD\7"+
		".\2\2D\22\3\2\2\2EG\5+\26\2FE\3\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2\2\2IK"+
		"\3\2\2\2JL\7\60\2\2KJ\3\2\2\2KL\3\2\2\2LP\3\2\2\2MO\5+\26\2NM\3\2\2\2"+
		"OR\3\2\2\2PN\3\2\2\2PQ\3\2\2\2Q\24\3\2\2\2RP\3\2\2\2ST\7=\2\2T\26\3\2"+
		"\2\2UV\7%\2\2V\30\3\2\2\2WX\7-\2\2X\32\3\2\2\2YZ\7B\2\2Z\34\3\2\2\2[\\"+
		"\7~\2\2\\\36\3\2\2\2]^\7V\2\2^ \3\2\2\2_`\7I\2\2`\"\3\2\2\2ab\7Z\2\2b"+
		"$\3\2\2\2cd\7u\2\2de\7m\2\2ef\7k\2\2fg\7r\2\2g&\3\2\2\2hj\t\2\2\2ih\3"+
		"\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2\2l(\3\2\2\2mo\t\3\2\2nm\3\2\2\2op\3"+
		"\2\2\2pn\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\b\25\2\2s*\3\2\2\2tu\t\4\2\2u,"+
		"\3\2\2\2\b\2HKPkp\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}