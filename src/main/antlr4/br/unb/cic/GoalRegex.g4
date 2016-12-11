grammar GoalRegex;

goal	:	expr NEWLINE							#printExpr                            
  		;

expr	:	GOALID ':' VAR TRY						#gTry
		|	GOALID ':' VAR							#gNoRT
		| 	GOALID ':' VAR op='[' RT op=']'			#gRT
		;
		
GOALID	:	'G'(DIGIT)+(WS)*					;
VAR		:	WS*(LETTER)+(LETTER|WS)* 			;
RT		:	(STR_EXT|WS)+						;
NEWLINE :	[\r\n]+             				;
WS	    :	(' '|'\t')+ -> skip 				;
TRY		:	'[try('SEQ+')?'SEQ+':'SEQ+']'		;

fragment
DIGIT		: [0-9]								;
LETTER		: [a-zA-Z_]							;
STR_EXT     : [a-zA-Z0-9_;#+@()|.]				;
SEQ			: [a-zA-Z0-9.]						;