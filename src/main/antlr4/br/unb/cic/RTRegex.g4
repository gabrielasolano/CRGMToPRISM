grammar RTRegex;

@rulecatch {
   catch (RecognitionException e) {
    throw e;
   }
}

rt:     expr NEWLINE                            # printExpr
  |     NEWLINE                                 # blank
  ;

expr:   expr op=('+'|'#'|'@') FLOAT				# gCard
    |	expr op='|' expr						# gAlt
    |	'opt(' expr ')'							# gOpt
    |   'try(' expr ')' '?' expr ':' expr		# gTry
    |	expr op=(';'|'#') expr					# gTime
    |   SKIPP									# gSkip
    |	t=('G'|'T') id							# gId
    |	'DM(' multiple ')'						# gDecisionMaking
    |   '(' expr ')'							# parens
    ;

multiple:	expr op=',' expr
	;
    
id:		FLOAT
	|	FLOAT X
	|	X
	;

FLOAT		: DIGIT+'.'?DIGIT* 	;
DM			: 'DM'				;
SEQ         : ';'				;
INT			: '#'				;
C_SEQ		: '+'				;
C_RTRY		: '@'				;
ALT			: '|'				;
TASK		: 'T'				;
GOAL		: 'G'				;
X			: 'X'				;
SKIPP		: 'skip'			;
NEWLINE 	: [\r\n]+           ;
WS          : [\t]+ -> skip 	;

fragment
DIGIT		: [0-9]				;
