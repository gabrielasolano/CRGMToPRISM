package br.unb.cic.rtgoretoprism.paramformula;

import java.util.ArrayList;
import java.util.List;

public class SymbolicParamOrGenerator {
	
	private int IDX = 1000;

	public String getSequentialOrCost(String[] nodes) {
		
		StringBuilder reliability = getReliability(nodes);
		StringBuilder formula = new StringBuilder();
		
		boolean isEven = false;
		if ((nodes.length % 2) == 0) isEven = true;
		
		String sign = "";
		if (isEven) {
			sign = "-";
			formula.append("( " + sign + " ");
		}
		else {
			formula.append("( ");
			sign = "+";
		}
		
		for (String node : nodes) {
			formula.append(reliability + " " + node + " " + sign);
		}
		
		formula.deleteCharAt(formula.length()-1);
		sign = changeSign(sign);

		if (nodes.length > 2) {
			for (int i = nodes.length - 1; i >= 2; i--) {
				formula.append(generateMultipleCombinationsCost(nodes, i, sign));
				sign = changeSign(sign);
			}
		}
			
		
		//Singles
		for (String node : nodes) {
			int id = getId(node);
			List<String> costs = getUsefulNodes(nodes, id);
			for (String cost : costs) {
				formula.append(" + R_" + node + " * " + cost);
			}
		}
        
		formula.append(" )");
		return formula.toString();
	}
	
public String getSequentialOrReliability(String[] nodes) {
		
		//StringBuilder reliability = getReliability(nodes);
		StringBuilder formula = new StringBuilder();
		
		boolean isEven = false;
		if ((nodes.length % 2) == 0) isEven = true;
		
		String sign = "";
		if (isEven) {
			sign = "-";
			formula.append("( " + sign + " ");
		}
		else {
			formula.append("( ");
			sign = "+";
		}
		
		//all nodes
		for (String node : nodes) {
			formula.append(node + " * ");
		}
		
		formula.deleteCharAt(formula.length()-2);
		sign = changeSign(sign);

		if (nodes.length > 2) {
			for (int i = nodes.length - 1; i >= 2; i--) {
				formula.append(generateMultipleCombinationsReliability(nodes, i, sign));
				sign = changeSign(sign);
			}
		}
		
		//Singles
		for (String node : nodes) {
			formula.append(" + " + node);
		}
        
		formula.append(" )");
		return formula.toString();
	}
	
	private String changeSign(String sign) {
		if (sign.equals("+")) return "-";
		return "+";
	}

	private StringBuilder generateMultipleCombinationsCost (String[] nodes, int numComb, String sign) {
		
		StringBuilder formula = new StringBuilder();
		
		List<String[]> list = new ArrayList<String[]>();
		GenerateCombination comb1 = new GenerateCombination(nodes, numComb) ;
        while (comb1.hasNext()) {
        	list.add(comb1.next());
        }
        
        for (String[] duple : list) {
        	int max = getMaxId(duple);
        	List<String> costs = getUsefulNodes(nodes, max);
        	
        	String prefix = sign;
        	for (int i = 0; i < duple.length; i++) {
        		prefix = prefix + " R_" + duple[i] + " * "; 
        	}
        	
        	for (String cost : costs) {
        		formula.append(prefix + cost + " ");
        	}
        }
        return formula;
	}
	
private StringBuilder generateMultipleCombinationsReliability (String[] nodes, int numComb, String sign) {
		
		StringBuilder formula = new StringBuilder();
		
		List<String[]> list = new ArrayList<String[]>();
		GenerateCombination comb1 = new GenerateCombination(nodes, numComb) ;
        while (comb1.hasNext()) {
        	list.add(comb1.next());
        }
        
        for (String[] duple : list) {
        	String prefix = " ";
        	for (int i = 0; i < duple.length; i++) {
        		prefix += duple[i] + " * "; 
        	}
        	prefix = prefix.substring(0, prefix.length()-2);
        	formula.append(" " + sign + prefix);
        }
        return formula;
	}

	private List<String> getUsefulNodes(String[] nodes, int max) {
		List<String> result = new ArrayList<String>();
		
		int id;
		for (String node : nodes) {
			id = getId(node);
			
			if (id <= max) result.add(node);
		}
		
		return result;
	}

	private int getId(String node) {
		int id;
		
		if (node.contains("X")) return IDX;
		if (node.contains("_")) {
			int last = node.lastIndexOf("_");
			id = Integer.parseInt(node.substring(last+1, node.length()));
		}
		else {
			id = Integer.parseInt(node.substring(1, node.length()));
		}
		return id;
	}

	private int getMaxId(String[] duple) {
		
		int max = getId(duple[0]);
		
		for (int i = 1; i < duple.length; i++) {
			int id = getId(duple[i]);
			if (id > max) max = id;
		}
		
		return max;
	}

	private StringBuilder getReliability(String[] nodes) {
		StringBuilder reliability = new StringBuilder();
		for (String node : nodes) {
			reliability.append(" R_" + node + " *");
		}
		return reliability;
	}

	//Provisional method until the formula pattern is understood
	public String getDecisionMakingFormula(String[] gids) {
		
		if (gids.length == 2) {
			return formulaForTwoNodes(gids);
		}
		else if (gids.length == 3) {
			return formulaForThreeNodes(gids);
		}
		else if (gids.length == 4) {
			return formulaForFourNodes(gids);
		}
		return "";
	}

	private String formulaForFourNodes(String[] gids) {
		String formula = new String();
		formula = "( ( - 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[3]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[0]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[1]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[2]
				+ " + 27 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[3]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[0]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[1]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[2]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[3]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[0]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[1]
				+ " + 27 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[2]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[3]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[0]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[1]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[2]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[3]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[0]
				+ " + 27 * R_" + gids[0] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[1]
				+ " + 30 * R_" + gids[0] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[2]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[3]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[0]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[1]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[2]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[3] + " * " + gids[3]
				+ " - 30 * R_" + gids[0] + " * R_" + gids[3] + " * " + gids[0]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[3] + " * " + gids[1]
				+ " - 25 * R_" + gids[0] + " * R_" + gids[3] + " * " + gids[2]
				+ " + 20 * R_" + gids[0] + " * " + gids[3]
				+ " + 30 * R_" + gids[0] + " * " + gids[0]
				+ " + 20 * R_" + gids[0] + " * " + gids[1]
				+ " + 20 * R_" + gids[0] + " * " + gids[2]
				+ " + 30 * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[3]
				+ " + 27 * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[0]
				+ " + 30 * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[1]
				+ " + 30 * R_" + gids[1] + " * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[2]				
				+ " - 25 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[3]
				+ " - 25 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[0]
				+ " - 30 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[1]
				+ " - 30 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[2]
				+ " - 30 * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[3]
				+ " - 25 * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[0]
				+ " - 30 * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[1]
				+ " - 25 * R_" + gids[1] + " * R_" + gids[3] + " * " + gids[2]
				+ " + 20 * R_" + gids[1] + " * " + gids[3]
				+ " + 20 * R_" + gids[1] + " * " + gids[0]
				+ " + 30 * R_" + gids[1] + " * " + gids[1]
				+ " + 20 * R_" + gids[1] + " * " + gids[2]			
				+ " - 30 * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[3]
				+ " - 25 * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[0]
				+ " - 25 * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[1]
				+ " - 30 * R_" + gids[2] + " * R_" + gids[3] + " * " + gids[2]
				+ " + 20 * R_" + gids[2] + " * " + gids[3]
				+ " + 20 * R_" + gids[2] + " * " + gids[0]
				+ " + 20 * R_" + gids[2] + " * " + gids[1]
				+ " + 30 * R_" + gids[2] + " * " + gids[2]
				+ " + 30 * R_" + gids[3] + " * " + gids[3]
				+ " + 20 * R_" + gids[3] + " * " + gids[0]
				+ " + 20 * R_" + gids[3] + " * " + gids[1]
				+ " + 20 * R_" + gids[3] + " * " + gids[2]
				+ " ) / 12 )";
		
		return formula;
	}

	private String formulaForThreeNodes(String[] gids) {
		String formula = new String();
		formula = "( ( 6 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[0]
				+ " + 6 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[1]
				+ " + 6 * R_" + gids[0] + " * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[2]
				+ " - 6 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[0]
				+ " - 6 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[1]
				+ " - 5 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[2]
				+ " - 6 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[0]
				+ " - 5 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[1]
				+ " - 6 * R_" + gids[0] + " * R_" + gids[2] + " * " + gids[2]
				+ " + 6 * R_" + gids[0] + " * " + gids[0]
				+ " + 4 * R_" + gids[0] + " * " + gids[1]
				+ " + 4 * R_" + gids[0] + " * " + gids[2]
				+ " - 5 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[0]
				+ " - 6 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[1]
				+ " - 6 * R_" + gids[1] + " * R_" + gids[2] + " * " + gids[2]
				+ " + 4 * R_" + gids[1] + " * " + gids[0]
				+ " + 6 * R_" + gids[1] + " * " + gids[1]
				+ " + 4 * R_" + gids[1] + " * " + gids[2]
				+ " + 4 * R_" + gids[2] + " * " + gids[0]
				+ " + 4 * R_" + gids[2] + " * " + gids[1]
				+ " + 6 * R_" + gids[2] + " * " + gids[2]
				+ " ) / 3 )";
				;
		
		return formula;
	}

	private String formulaForTwoNodes(String[] gids) {
		String formula = new String();
		formula = "( ( - 3 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[0]
				+ " - 3 * R_" + gids[0] + " * R_" + gids[1] + " * " + gids[1]
				+ " + 3 * R_" + gids[0] + " * " + gids[0]
				+ " + 2 * R_" + gids[0] + " * " + gids[1]
				+ " + 2 * R_" + gids[1] + " * " + gids[0]
				+ " + 3 * R_" + gids[1] + " * " + gids[1]
				+ " ) / 2 )";
		
		return formula;
	}

}
