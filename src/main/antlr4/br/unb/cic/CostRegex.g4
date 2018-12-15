grammar CostRegex;

@rulecatch {
   catch (RecognitionException e) {
    throw e;
   }
}

cost:	'C' op='=' FLOAT			# gCost
	|	'C' op='=' FLOAT VAR		# gVariable
 	|	NEWLINE						# blank
  	;

FLOAT		: DIGIT+'.'?DIGIT* 			;
VAR     	: ('a'..'z'|'A'..'Z'|'_')+	;
NEWLINE 	: [\r\n]+           		;
WS          : [\t]+ -> skip 			;

fragment
DIGIT		: [0-9]						;
