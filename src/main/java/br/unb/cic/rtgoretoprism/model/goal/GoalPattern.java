package br.unb.cic.rtgoretoprism.model.goal;

public class GoalPattern {
	String identifier;
	String description;
	String rt;
	
	public GoalPattern(String identifier, String description, String rt) {
		this.identifier = identifier;
		this.description = description;
		this.rt = rt;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	
}
