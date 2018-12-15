grammar CostRegex;

@rulecatch {
   catch (RecognitionException e) {
    throw e;
   }
}

cost:     'C' op='=' FLOAT				# gCost
 	|     NEWLINE                       # blank
  	;

FLOAT		: DIGIT+'.'?DIGIT* 	;
NEWLINE 	: [\r\n]+           ;
WS          : [\t]+ -> skip 	;

fragment
DIGIT		: [0-9]				;
