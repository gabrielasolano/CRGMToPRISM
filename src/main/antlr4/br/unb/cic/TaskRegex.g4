grammar TaskRegex;

task		:	expr NEWLINE						#printExpr 
			;

expr		:	taskid ':' VAR TRY					#tTry
			|	taskid ':' VAR op='[' RT op=']'			#tRT				
			|	taskid ':' VAR							#tNoRT
			;
			
taskid		:	TASKINT								#tInt
			|	TASKFLOAT							#tFloat
			;

TASKINT		:	'T'(DIGIT)+(WS)*					;
TASKFLOAT	:	'T'DIGIT+'.'DIGIT+(WS)*				;
VAR			:	WS*(LETTER)+(LETTER|WS)* 					;
RT			:	(STR_EXT|WS)+								;
NEWLINE 	: 	[\r\n]+             						;
WS	        : 	(' '|'\t')+ -> skip 						;
TRY			:	'[try('SEQ+')?'SEQ+':'SEQ+']'				;


fragment
DIGIT		: [0-9]								;
LETTER		: [a-zA-Z_]							;
STR_EXT     : [a-zA-Z0-9_;#+@()|.]				;
SEQ			: [a-zA-Z0-9.]						;