grammar CtxRegex;

@rulecatch {
   catch (RecognitionException e) {
    throw e;
   }
}

ctx:	ctx NEWLINE					# printExpr
	|	'assertion condition 'expr 	# condition
	|	'assertion trigger 'expr 	# trigger
	|	NEWLINE                     # blank
	;

expr:	VAR op='<' value			#cLT
	|	VAR op='<=' value			#cLE
	|	VAR op='>' value			#cGT
	|	VAR op='>=' value			#cGE
	|	VAR op='=' value			#cEQ
	|	VAR op='!=' value			#cDIFF
	|	VAR op='&' VAR				#cAnd
	|	VAR op='|' VAR				#cOr
	| 	VAR							#cVar
	;

value:	FLOAT						#cFloat
	|	BOOL						#cBool
	;

VAR     	: LETTER+DIGIT*WS*			;
FLOAT		: DIGIT+'.'?DIGIT* 			;
BOOL		: WS*('false'|'true')WS*	;
NEWLINE 	: [\r\n]+     				;
WS	        : (' '|'\t')+ -> skip 		;

fragment
DIGIT		: [0-9]						;
LETTER		: [a-zA-Z_]					;
