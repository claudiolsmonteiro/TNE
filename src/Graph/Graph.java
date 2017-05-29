package Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.print.DocFlavor.STRING;

import jade.core.Agent;

public class Graph {
	//Map<String,Agent> nodes;
	List<String> nodes;
	List<Edge> edges;
	
	public Graph() {
		nodes = new ArrayList<String>();
		edges = new ArrayList<Edge>();
		
	}
	
	public void AddNode(String s/*Agent n*/) {
		//nodes.put(n.getName(), n);
		nodes.add(s);
	}
	
	public void AddEdge( String in, String out, float w) {
		edges.add(new Edge(in, out, w));
	}
	
	
	public float PageRank(float alpha, String target){
		
		List<String> visited = new ArrayList<String>();

		String current;
		
		//escolher um node para ser o inicial
		Random r = new Random();
		int index = r.nextInt(nodes.size());
		
		String initial = nodes.get(index);
		current = initial;
		visited.add(current);
		
		int steps = 1;
		float terminate = r.nextFloat();
		while(terminate > alpha){
			steps++;
			List<String> next = new ArrayList<String>();
			List<Float> nextProb = new ArrayList<Float>();
			float prob = 0;
			
			for(int i = 0; i < edges.size(); i++){
				if(edges.get(i).getFrom().equals(current)){
					prob += getRandomWalkProbability(edges.get(i).getFrom(), edges.get(i).getTo());
					next.add(edges.get(i).getTo());
					nextProb.add(prob);
				}
			}
			
			float step = r.nextFloat();
			for(int i = 0; i < nextProb.size(); i++){
				if(nextProb.get(i) >= step){
					current = next.get(i);
					i = nextProb.size();
				}
			}
			visited.add(current);
			terminate = r.nextFloat();
		}
			
		int count = 0;
		System.out.println("Visited: ");
		for(int i = 0; i < visited.size(); i++){
			if(visited.get(i).equals(target)){
				count++;
			}
		}
		return (float)count/steps;
	}
	
	
	public int PersonalizedPageRank(){
		return 0;
	}
	public int GlobalHittingTime(){
		return 0;
	}
	public int PersonalizedHittingTime(){
		return 0;
	}
	public float AverageScore(String target){
		float result = 0;
		int den = 0;
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).isTarget(target)){
				result += edges.get(i).getWeight();
				den++;
			}
		}
		return result/den;
	}
	
	public void print(){
		System.out.println("----------graph info----------");
		System.out.println("Number of nodes: "+nodes.size());
		for(int i = 0; i < nodes.size(); i++){
			System.out.println(nodes.get(i));
		}
		System.out.println("Number of edges: "+edges.size());
		for(int i = 0; i < edges.size(); i++){
			System.out.println("from: " + edges.get(i).getFrom() + " | to: " + edges.get(i).getTo() + " | w: " + edges.get(i).getWeight());
		}
		System.out.println("------------------------------");
	}
	
	
	
	private float getRandomWalkProbability(String current, String next){
		
		int index = -1;
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getFrom().equals(current) && edges.get(i).getTo().equals(next)){
				index = i;
			}
			
		}
		
		if(index > -1){
			float w = edges.get(index).getWeight();
			float sum = 0;
			for(int i = 0; i < edges.size(); i++){
				if(edges.get(i).getFrom().equals(current)){
					sum += edges.get(i).getWeight();
				}
			}
			return w/sum;
		}
		return 0;
	}
}
