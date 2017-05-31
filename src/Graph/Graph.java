package Graph;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.print.DocFlavor.STRING;

import jade.core.Agent;

public class Graph {
	//Map<String,Agent> nodes;
	List<Node> nodes;
	List<Edge> edges;
	double[][] transitionMatrix; 
	
	
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public void AddNode(String s/*Agent n*/) {
		//nodes.put(n.getName(), n);
		nodes.add(new Node(s));
	}
	
	public void AddEdge( String in, String out, double w) {
		
		Edge e = new Edge(in, out, w);
		edges.add(e);
		
		for(int i = 0; i < nodes.size(); i++){
			if(nodes.get(i).getName().equals(in))
				nodes.get(i).addOutEdge(e);
		}
	}
	
	public int PageRank(String target){
		
		double[] fq = new double[nodes.size()];
		
		double value =(double) 1/nodes.size();
		
		for(int i = 0; i < fq.length; i++){
			 fq[i] = value;
		}

		
		double[] result = getSteadyState(fq, transitionMatrix);
		
		
		int index = getIndex(target) ;
		int rank = 1;
		for(int i = 0; i < result.length; i++) {
			if(result[i]>result[index])
				rank++;
		}

		return rank;
	}
	
	
	public int PersonalizedPageRank(String target, int agents){
		double[] fq = new double[nodes.size()];
		
		double value[] = new double[nodes.size()];
		for(int i = 0; i < value.length; i++)
			value[i]=0.0;
		
		int index = getIndex(target);
		
		double[] result;
		for(int j = 0; j < agents; j++) {
			if(!target.equals("Agent"+j)) {
				for(int i = 0; i < fq.length; i++){
					if(i == j) {
						 fq[i] = (double)1;
					}
					else {
						fq[i]=(double)0;
					}
				}
				result = getSteadyState(fq, transitionMatrix);
				for(int i = 0; i < value.length; i++){
					value[i] += result[i];
				}
					
			}
		}
		
		int rank = 1;
		for(int i = 0; i < value.length; i++) {
			if(value[i]>value[index])
				rank++;
		}
		return rank;
	}
	public int getIndex(String target) {
		int index = -1;
		for(int i = 0; i < nodes.size(); i++){
			 if(nodes.get(i).getName().equals(target))
				 index = i;
		}
		return index;
	}
	public int GlobalHittingTime(){
		return 0;
	}
	public int PersonalizedHittingTime(){
		return 0;
	}
	public int AverageScore(String target){
		double result[] = new double[nodes.size()];
		int rank = 1;
		int index = getIndex(target);
		for(int i = 0; i < nodes.size(); i++) {
			double average = 0, nEdges = 0;
			for(int j = 0; j < edges.size(); j++){
				if(edges.get(j).getTo().equals(nodes.get(i).getName())) {
					average += edges.get(j).getWeight();
					nEdges++;
				}
			}
			result[i] = (double)average/nEdges;
		}
		for(int i = 0; i < result.length; i++) {
			if(result[i]>result[index]) {
				rank++;
			}
		}
		return rank;
	}
	
	public void print(){
		System.out.println("----------graph info----------");
		System.out.println("Number of nodes: "+nodes.size());
		System.out.println("Number of edges: "+edges.size());
		DecimalFormat df = new DecimalFormat("0.000");
		/*for(int i = 0; i < edges.size(); i++){
			System.out.println("from: " + edges.get(i).getFrom() + " | to: " + edges.get(i).getTo() + " | w: " + df.format(edges.get(i).getWeight()));
		}*/
		
		//System.out.println("");
		//System.out.println("-------TransitionMatrix-------");
		
		for(int row = 0; row < nodes.size(); row++){
			System.out.print(row+": ");
			for(int col = 0; col < nodes.size(); col++){
				System.out.print(df.format(transitionMatrix[row][col]) + "|");
			}
			System.out.println("");
		}
		
		System.out.println("------------------------------");
	}
	
	
	
	
	public void SetTransitionMatrix(double alpha){
		int size = nodes.size();
		transitionMatrix = new double[size][size];
		
		double multiplier = 1-alpha;
		
		for(int row = 0; row < size; row++){
			
			Node node = nodes.get(row);
			
			for(int col = 0; col < size; col++){
				
				//double w = node.getEdgeWeight("True"+col);
				//double den = node.getOutEdges().size();
				
				double w = getRandomWalkProbability(node.getName(), "Agent"+col, alpha);
				transitionMatrix[row][col] = w;
			}
		}
	}

	public double[] multiplyMatrixes(double[] fq, double[][] m){
		double[] result = new double[fq.length];
		
		for(int row = 0; row < fq.length; row++){
			
			double value = 0;
			
			for(int col = 0; col < fq.length; col++){
				value += (double)fq[col]*m[col][row];
			}
			result[row] = value;
		}
		
		return result;
	}

	public boolean compareArray(double [] a, double[] b) {
		DecimalFormat df = new DecimalFormat("0.000000000000");
		boolean isequal= true;
		for(int i = 0; i< a.length; i++) {
			if(!df.format(a[i]).equals(df.format(b[i]))) {
				return false;
			}
		}
		
		return isequal;
	}
	
	public double[] getSteadyState(double[] fq, double[][] m){
		
		
		double[] steady1 = fq;
		double[] steady2 = multiplyMatrixes(fq, m);
		
		
		while(!compareArray(steady1, steady2)){
			steady1 = steady2;
			steady2 = multiplyMatrixes(steady1, m);
		}
		
		return steady2;
	}
	
	
	public double getSteadyState(String target, String view){
		
		System.out.println("------------Steady State First--------");
		
		int indexTarget=-1;
		int indexView=-1;
		for(int i=0; i < nodes.size(); i++){
			if(nodes.get(i).getName().equals(target))
				indexTarget = i;
			if(nodes.get(i).getName().equals(view))
				indexView = i;
		}
		
		double[] fq = new double[nodes.size()];
		
		
		for(int i = 0; i < nodes.size(); i++){
			
			fq[i] = (double)1/nodes.size();
			
			if(indexView > 0){
				if(i == indexView)
					fq[i] = 1;
				else
					fq[i] = 0;
			}
		}
		
		return getSteadyState(fq, transitionMatrix)[indexTarget];
	}
	
	private double getRandomWalkProbability(String current, String next, double alpha){
		
		int index = -1;
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getFrom().equals(current) && edges.get(i).getTo().equals(next)){
				index = i;
			}
			
		}
		
		double multiplier = (double)1-alpha;
		double result = (double)alpha/nodes.size();
		
		if(index > -1){
			double w = edges.get(index).getWeight();
			double sum = 0;
			for(int i = 0; i < edges.size(); i++){
				if(edges.get(i).getFrom().equals(current)){
					sum += edges.get(i).getWeight();
				}
			}
			double value = multiplier * w/sum;
			result += value;
		}
		return result;
	}

	public void cutOutlinks(String string, int agents) {
		// TODO Auto-generated method stub
		for(int i = 0; i < edges.size(); i++){
			String from = edges.get(i).getFrom();
			String to = edges.get(i).getTo();
			
			int toIndex = Integer.parseInt(to.split("t")[1]);

			if(from.equals(string) && toIndex < agents){
				edges.remove(i);
				i--;
			}
		}
	}

	public void reportZero(String string) {
		// TODO Auto-generated method stub
		for(int i = 0; i < nodes.size(); i++){
			String from = string;
			String to ="Agent"+i;
			double w = (double)0;
			if(!from.equals(to))
				edges.add(new Edge(from,to,w));
		}
	}
}
