package Agents;

import java.util.StringTokenizer;
import java.util.Vector;
import Graph.Graph;
import Agents.TrueAgent.FIPAContractNetResp;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ReputationAgent extends Agent {
	
	public Graph g, gsybil;
	
	public void setup() {
		g = new Graph();
		gsybil = new Graph();
		addBehaviour(new FIPAContractNetInit(this, new ACLMessage(ACLMessage.CFP)));
		
	}
	
	
	//-------------------------------------------FIPA--------------------------------------

	class FIPAContractNetInit extends ContractNetInitiator {

		public FIPAContractNetInit(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		protected Vector prepareCfps(ACLMessage cfp) {
			Vector v = new Vector();
			
			for(int i = 0; i < 100; i++) {
					cfp.addReceiver(new AID("Agent"+i, false));
					g.AddNode("Agent"+i);
					gsybil.AddNode("Agent"+i);
			}
			for(int i = 100; i < 110; i++) {
				cfp.addReceiver(new AID("Sybil"+(i-100), false));
				gsybil.AddNode("Agent"+i);
		}
			cfp.setContent(myAgent.getLocalName());

			v.add(cfp);
			return v;
		}

		protected void handleAllResponses(Vector responses, Vector acceptances) {
			
			
			for(int i=0; i<responses.size(); i++) {
				ACLMessage resp = ((ACLMessage) responses.get(i));
				
				String from = resp.getSender().getLocalName();

				StringTokenizer st = new StringTokenizer(resp.getContent(), "|");

				int t = 0;
				
				while(st.hasMoreTokens()){
					String to = "Agent"+t;
					String weight = st.nextToken();
					
					if(!weight.equals("-") && !from.equals(to)){
						if(from.contains("Sybil")) {
							String[] parts = from.split("l");
							String part2 = parts[1]; 
							from = "Agent"+(Integer.parseInt(part2)+100);
							gsybil.AddEdge(from, to, Double.parseDouble(weight));
							gsybil.AddEdge(to, from, Double.parseDouble(weight));
						}
						else {
							g.AddEdge(from, to, Double.parseDouble(weight));
							gsybil.AddEdge(from, to, Double.parseDouble(weight));
							
						}

					}
					t++;
				}
			}
			g.SetTransitionMatrix(0.15);
			g.print();
			gsybil.SetTransitionMatrix(0.15);
			gsybil.print();

			System.out.println("-------------Without Sybils---------------------");
			System.out.println("PageRank Score with sybils of True0: "+g.PageRank("Agent0"));
			System.out.println("Personalized PageRank Score of True0: "+g.PersonalizedPageRank("Agent0",100));
			System.out.println("Average Score with sybils of True0: "+g.AverageScore("Agent0"));
			System.out.println("----------------With Sybils----------------------");
			System.out.println("PageRank Score of True0: "+gsybil.PageRank("Agent0"));
			System.out.println("Personalized PageRank Score of True0: "+gsybil.PersonalizedPageRank("Agent0",100));
			System.out.println("Average Score with sybils of True0: "+gsybil.AverageScore("Agent0"));
			System.out.println("--------------With Constraints------------------------");
			gsybil.cutOutlinks("Agent0",100);
			g.cutOutlinks("Agent0",100);
			g.SetTransitionMatrix(0.15);
			gsybil.SetTransitionMatrix(0.15);
			System.out.println("PageRank Score without sybils of True0: "+g.PageRank("Agent0"));
			System.out.println("Personalized PageRank without sybils Score of True0: "+g.PersonalizedPageRank("Agent0",100));
			System.out.println("PageRank Score with sybils of True0 with cutting outlinks: "+gsybil.PageRank("Agent0"));
			System.out.println("Personalized PageRank Score of True0: "+gsybil.PersonalizedPageRank("Agent0",100));
			gsybil.reportZero("Agent0");
			gsybil.SetTransitionMatrix(0.15);
			System.out.println("Average Score with sybils of True0 with report 0: "+gsybil.AverageScore("Agent0"));
		}
		
		protected void handleAllResultNotifications(Vector resultNotifications) {
			System.out.println("got " + resultNotifications.size() + " result notifs!");
		}
		
	}



}
