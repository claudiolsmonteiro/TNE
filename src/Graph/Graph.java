package Graph;

import java.util.List;
import java.util.Map;

import jade.core.Agent;

public class Graph {
	Map<String,Agent> nodes;
	List<Edge> edges;
	
	public Graph() {
		
	}
	
	public void AddNode(Agent n) {
		nodes.put(n.getName(), n);
	}
	
	public void AddEdge( Edge e) {
		edges.add(e);
	}
	
	
	
}
