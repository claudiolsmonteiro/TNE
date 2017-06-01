package Graph;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.print.DocFlavor.STRING;

import org.la4j.Matrix;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.dense.Basic2DMatrix;

import jade.core.Agent;

public class Graph {
	//Map<String,Agent> nodes;
	List<Node> nodes;
	List<Edge> edges;
	double[][] transitionMatrix; 
	static double alpha;
	
	
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
	
	static double[][] IntermediateStep(double[][] m1){
	        double[][] ret = new double[m1.length][m1.length];
	       
	       
	        for(int i = 0; i < m1.length; i++){
	            for(int j = 0; j < m1.length; j++){
	                ret[i][j] = ((1-alpha) * m1[i][j]);
	            }
	        }
	        return ret;
	    }
	 
	static double[][] FundamentalMatrix(double[][] m1){
	       
	        double[][] ret = IntermediateStep(m1);
	       
	        for(int i = 0; i < m1.length; i++){
	            for(int j = 0; j < m1.length; j++){
	                if(i == j)
	                    ret[i][j] = 1 - ret[i][j];
	                else
	                    ret[i][j] = 0 - ret[i][j];
	            }
	        }
	        return ret;
	    }
	    
	public double[] PageRank(String target){
		
		double [][]m = FundamentalMatrix(transitionMatrix);
        Matrix inv = new Basic2DMatrix(m);
        GaussJordanInverter v = new GaussJordanInverter(inv);
        Matrix result = v.inverse();
       
        double[][] N = new double[m.length][m.length];
        for(int i = 0; i< m.length; i++){
            for(int j = 0; j< m.length; j++){
                N[i][j] = result.get(i, j);
            }
        }
		
        double [] pr = PR(N);
		int index = getIndex(target) ;
		int rank = 1;
		for(int i = 0; i < pr.length; i++) {
			if(pr[i]>pr[index])
				rank++;
		}

		double[] ret = {rank, pr[index]};
		return ret;
	}
	
	  static double[][] PPR (double[][] m1){
	       
	        double[] h = new double[m1.length];
	        double[][] ppr = new double[m1.length][m1.length];
	       
	        for(int i = 0; i < ppr.length; i++){
	            h[i] = 0;
	            for(int j = 0; j < ppr.length; j++){
	                h[i] += m1[i][j];
	               
	            }
	            for(int j = 0; j < ppr.length; j++){
	               
	                if(i != j){
	                    ppr[i][j] = (double)m1[i][j]/h[i];
	                }
	               
	            }
	        }
	        return ppr;
	    }
	   
	    static double[] PR (double[][] m1){
	       
	        double[] h = new double[m1.length];
	        double[] pr = new double[m1.length];
	       
	        for(int i = 0; i < pr.length; i++){
	            h[i] = 0;
	            pr[i] = 0;
	            for(int j = 0; j < pr.length; j++){
	                h[i] = h[i] + m1[i][j];
	            }
	        }
	       
	        for(int j = 0; j < pr.length; j++){
	           
	            for(int i = 0; i < pr.length; i++){
	               
	                if(i != j){
	                    pr[j] += (double)m1[i][j]/h[i];
	                }
	            }
	           
	            pr[j] = (double)1/(m1.length-1) * pr[j];
	        }
	       
	        return pr;
	    }
	   
	   
	public double[] PersonalizedPageRank(String target, int agents){
		double [][]m = FundamentalMatrix(transitionMatrix);
        Matrix inv = new Basic2DMatrix(m);
        GaussJordanInverter v = new GaussJordanInverter(inv);
        Matrix result = v.inverse();
        double[][] N = new double[m.length][m.length];
        for(int i = 0; i< m.length; i++){
            for(int j = 0; j< m.length; j++){
                N[i][j] = result.get(i, j);
            }
        }

        double [][] ppr = PPR(N);
		double [] average = new double[agents];
		for(int i = 0; i < agents; i++)
			average[i] = 0;
		int rank = 1, index = getIndex(target);
		for(int i = 0; i < average.length; i++) {
			
			for(int j = 0; j < average.length; j++ ) {
					average[i] += ppr[j][i];	
			}
			
		}
		for(int i = 0; i < average.length; i++) {
			if(average[i] > average[index]) 
				rank++;
		}
		double[] ret = {rank, average[index]};
		return ret;
	}
	public int getIndex(String target) {
		int index = -1;
		for(int i = 0; i < nodes.size(); i++){
			 if(nodes.get(i).getName().equals(target))
				 index = i;
		}
		return index;
	}
	public double[] GlobalHittingTime(String target){
		
		double [][]m = FundamentalMatrix(transitionMatrix);
        Matrix inv = new Basic2DMatrix(m);
        GaussJordanInverter v = new GaussJordanInverter(inv);
        Matrix result = v.inverse();
       
        double[][] N = new double[m.length][m.length];
        for(int i = 0; i< m.length; i++){
            for(int j = 0; j< m.length; j++){
                N[i][j] = result.get(i, j);
            }
        }
		
        double [] ght = ght(N);
		int index = getIndex(target) ;
		int rank = 1;
		for(int i = 0; i < ght.length; i++) {
			if(ght[i]>ght[index])
				rank++;
		}
		double[] ret = {rank, ght[index]};
		return ret;
	}
	
	public double [] ght(double [][]N) {
		double []ght = new double[N.length];
		
		for(int j = 0; j < N.length; j++) {
			for(int i = 0; i < N.length; i++) {
				if ( i != j)
					ght[j] = ght[j] + (N[i][j]/N[j][j]);
			}
			ght[j] = ght[j]/(N.length-1);
		}
		return ght;
	}
	
	public double[] PersonalizedHittingTime(String target, int agents){
		double [][]m = FundamentalMatrix(transitionMatrix);
        Matrix inv = new Basic2DMatrix(m);
        GaussJordanInverter v = new GaussJordanInverter(inv);
        Matrix result = v.inverse();
        double[][] N = new double[m.length][m.length];
        for(int i = 0; i< m.length; i++){
            for(int j = 0; j< m.length; j++){
                N[i][j] = result.get(i, j);
            }
        }

        double [][] pht = pht(N);
		double [] average = new double[agents];
		for(int i = 0; i < agents; i++)
			average[i] = 0;
		int rank = 1, index = getIndex(target);
		for(int i = 0; i < average.length; i++) {
			
			for(int j = 0; j < average.length; j++ ) {
					average[i] += pht[j][i];	
			}
			
		}
		for(int i = 0; i < average.length; i++) {
			if(average[i] > average[index]) 
				rank++;
		}
		
		double[] ret = {rank, average[index]};
		return ret;
	}
	
	public double [][] pht(double [][]N) {
		double [][] pht = new double[N.length][N.length];
		for(int i = 0; i< N.length; i++) {
			for(int j = 0; j < N.length; j++)
				if(i!=j)
					pht[i][j] = N[i][j]/N[j][j];
		}
		return pht;
	}
	
	public double[] AverageScore(String target){
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
		double[] ret = {rank, result[index]};
		return ret;
	}
	
	public void print(){
		System.out.println("----------graph info----------");
		System.out.println("Number of nodes: "+nodes.size());
		System.out.println("Number of edges: "+edges.size());
		DecimalFormat df = new DecimalFormat("0.000");
		
		for(int row = 0; row < nodes.size(); row++){
			System.out.print(row+": ");
			for(int col = 0; col < nodes.size(); col++){
				System.out.print(df.format(transitionMatrix[row][col]) + "|");
			}
			System.out.println("");
		}
		
		System.out.println("------------------------------");
	}
	
	
	
	
	public void SetTransitionMatrix(){
		int size = nodes.size();
		transitionMatrix = new double[size][size];
		
		
		for(int row = 0; row < size; row++){
			
			Node node = nodes.get(row);
			
			for(int col = 0; col < size; col++){
				
				double w = getRandomWalkProbability(node.getName(), "Agent"+col);
				transitionMatrix[row][col] = w;
			}
		}
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
	
	
	
	
	private double getRandomWalkProbability(String current, String next){
		
		int index = -1;
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getFrom().equals(current) && edges.get(i).getTo().equals(next)){
				index = i;
			}
			
		}
		
		double result = 0;
		
		if(index > -1){
			double w = edges.get(index).getWeight();
			double sum = 0;
			for(int i = 0; i < edges.size(); i++){
				if(edges.get(i).getFrom().equals(current)){
					sum += edges.get(i).getWeight();
				}
			}
			return (double)w/sum;
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

	public void setAlpha(double d) {
		alpha = d;
		// TODO Auto-generated method stub
		
	}
}
