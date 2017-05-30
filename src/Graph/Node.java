package Graph;

import java.util.ArrayList;
import java.util.List;

public class Node {

	String name;
	List<Edge> out;
	
	public Node(String s){
		name = s;
		out = new ArrayList<Edge>();
	}
	
	public String getName(){
		return name;
	}
	
	public List<Edge> getOutEdges(){
		return out;
	}
	
	public double getEdgeWeight(String s){
		
		for(int e = 0; e < out.size(); e++){
			Edge edge = out.get(e);
			if(edge.getTo().equals(s)){
				return edge.getWeight();
			}
		}
		return 0;
	}
	
	public void addOutEdge(Edge e){
		out.add(e);
	}
	
	
}
