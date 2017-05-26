package Graph;

import jade.core.Agent;

public class Edge {
	Agent in, out;
	double weight;
	
	public Edge(Agent i, Agent o, double w){
		in = i;
		out = o;
		weight = w;
	}
	
	public void changeWeight(double w) {
		weight = w;
	}
}
