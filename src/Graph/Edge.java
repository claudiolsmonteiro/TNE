package Graph;

import jade.core.Agent;

public class Edge {
	String from, to;
	double weight;
	
	public Edge(String i, String o, double w){
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
	
	public double getWeight() {
		return weight;
	}
	
	
	public boolean isTarget(String s){
		return to.equals(s);
	}
}
