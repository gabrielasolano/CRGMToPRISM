#!/bin/bash

# Log Location on Server.
#LOG_LOCATION=/home/gabriela/Desktop/Experiments
#exec > >(tee -i $LOG_LOCATION/ParamAND2.txt)
#exec 2>&1

#echo "Log Location should be: [ $LOG_LOCATION ]"
#echo

TOTAL=0
chmod +x eval_formula.sh
for i in {1..100};
do
	#echo "Evaluating reliability formula"
	START=$(date +%s.%N)
	./eval_formula.sh reliability.out
	END=$(date +%s.%N)
	DIFF=$(expr $END-$START)
	TOTAL=$(expr $TOTAL+$DIFF)
	#printf "\n"
	#echo 'Reliability evaluated in '$DIFF 'ms'
	#printf "\n"
done
echo 'RELIABILITY EVALUATION AVERAGE IS '$(echo "scale=10;($TOTAL)/100" | bc) 's'.
echo

TOTAL=0
for i in {1..100};
do
	#echo "Evaluating cost formula"
	START=$(date +%s.%N)
	./eval_formula.sh cost.out
	END=$(date +%s.%N)
	DIFF=$(expr $END-$START)
	TOTAL=$(expr $TOTAL+$DIFF)
	#printf "\n"
	#echo 'Cost evaluated in '$($TOTAL/100) 'ms'
	#printf "\n"
done

echo 'COST EVALUATION AVERAGE IS '$(echo "scale=10;($TOTAL)/100" | bc) 's'.
echo