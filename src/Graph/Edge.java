package Graph;

import jade.core.Agent;

public class Edge {
	String from, to;
	float weight;
	
	public Edge(String i, String o, float w){
		from = i;
		to = o;
		weight = w;
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getTo(){
		return to;
	}
	
	public void changeWeight(float w) {
		weight = w;
	}
	
	public float getWeight() {
		return weight;
	}
	
	
	public boolean isTarget(String s){
		return to.equals(s);
	}
}
