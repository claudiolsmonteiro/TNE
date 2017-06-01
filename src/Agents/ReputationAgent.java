package Agents;

import java.util.StringTokenizer;
import java.util.Vector;
import Graph.Graph;
import PublicVar.PublicVariable;
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
			
			for(int i = 0; i < PublicVariable.getNagents(); i++) {
					cfp.addReceiver(new AID("Agent"+i, false));
					g.AddNode("Agent"+i);
					gsybil.AddNode("Agent"+i);
			}
			int extraSybils = (int)Math.ceil((double)PublicVariable.getNsybils()*0.75);
			for(int i = PublicVariable.getNagents(); i < (PublicVariable.getNagents()+PublicVariable.getNsybils()+extraSybils); i++) {
				cfp.addReceiver(new AID("Sybil"+(i-PublicVariable.getNagents()), false));
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
							from = "Agent"+(Integer.parseInt(part2)+PublicVariable.getNagents()); //Agent NAgents+SybilIndex
							
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

			g.setAlpha(0.15);
			g.SetTransitionMatrix();
			g.print();
			gsybil.setAlpha(0.15);
			gsybil.SetTransitionMatrix();
			gsybil.print();

			
			System.out.println("-------------Without Sybils---------------------");
			System.out.println("PageRank of True0: " + g.PageRank("Agent0")[0] + "                      | " + g.PageRank("Agent0")[1]);
			System.out.println("PageRank of True1: " + g.PageRank("Agent1")[0] + "                      | " + g.PageRank("Agent1")[1]);

			System.out.println("Personalized PageRank of True0: "+g.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[0] + "          | " + g.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized PageRank of True1: "+g.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[0] + "          | " + g.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Global Hitting time Rank of True0: "+g.GlobalHittingTime("Agent0")[0] + "      | " + g.GlobalHittingTime("Agent0")[1]);
			System.out.println("Global Hitting time Rank of True1: "+g.GlobalHittingTime("Agent1")[0] + "      | " + g.GlobalHittingTime("Agent1")[1]);

			System.out.println("Personalized Hitting Time Rank of True0: "+g.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[0] + " | " + g.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized Hitting Time Rank of True1: "+g.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[0] + " | " + g.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Average Score Rank of True0: "+g.AverageScore("Agent0")[0] + "            | " + g.AverageScore("Agent0")[1]);
			System.out.println("Average Score Rank of True1: "+g.AverageScore("Agent1")[0] + "            | " + g.AverageScore("Agent1")[1]);

			
			System.out.println("----------------With Sybils----------------------");
			System.out.println("PageRank of True0: "+gsybil.PageRank("Agent0")[0] + "                        | " + gsybil.PageRank("Agent0")[1]);
			System.out.println("PageRank of True1: "+gsybil.PageRank("Agent1")[0] + "                        | " + gsybil.PageRank("Agent1")[1]);

			System.out.println("Personalized PageRank of True0: "+gsybil.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[0] + "           | " + gsybil.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized PageRank of True1: "+gsybil.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[0] + "           | " + gsybil.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Global Hitting time Rank of True0: "+gsybil.GlobalHittingTime("Agent0")[0] + "        | " + gsybil.GlobalHittingTime("Agent0")[1]);
			System.out.println("Global Hitting time Rank of True1: "+gsybil.GlobalHittingTime("Agent1")[0] + "        | " + gsybil.GlobalHittingTime("Agent1")[1]);

			System.out.println("Personalized Hitting Time Rank of True0: "+gsybil.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[0] + " | " + gsybil.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized Hitting Time Rank of True1: "+gsybil.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[0] + " | " + gsybil.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Average Score Rank of True0: "+gsybil.AverageScore("Agent0")[0] + "             | " + gsybil.AverageScore("Agent0")[1]);
			System.out.println("Average Score Rank of True1: "+gsybil.AverageScore("Agent1")[0] + "             | " + gsybil.AverageScore("Agent1")[1]);

			
			
			gsybil.cutOutlinks("Agent0",PublicVariable.getNagents());
			g.cutOutlinks("Agent0",PublicVariable.getNagents());
			gsybil.cutOutlinks("Agent1",PublicVariable.getNagents());
			g.cutOutlinks("Agent1",PublicVariable.getNagents());
			g.SetTransitionMatrix();
			gsybil.SetTransitionMatrix();
			
			System.out.println("--------------Cutting Outlinks------------------------");
			System.out.println("PageRank of True0: "+g.PageRank("Agent0")[0] + "                      | " + g.PageRank("Agent0")[1]);
			System.out.println("PageRank of True1: "+g.PageRank("Agent1")[0] + "                      | " + g.PageRank("Agent1")[1]);

			System.out.println("Personalized PageRank of True0: "+g.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[0] + "         | " + g.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized PageRank of True1: "+g.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[0] + "         | " + g.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Global Hitting time Rank of True0: "+g.GlobalHittingTime("Agent0")[0] + "       | " + g.GlobalHittingTime("Agent0")[1]);
			System.out.println("Global Hitting time Rank of True1: "+g.GlobalHittingTime("Agent1")[0] + "       | " + g.GlobalHittingTime("Agent1")[1]);

			System.out.println("Personalized Hitting Time Rank of True0: "+g.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[0] + " | " + g.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized Hitting Time Rank of True1: "+g.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[0] + " | " + g.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[1]);

			g.reportZero("Agent0");
			g.reportZero("Agent1");
			g.SetTransitionMatrix();
			System.out.println("Average Score Rank of True0: "+g.AverageScore("Agent0")[0] + "             | " + g.AverageScore("Agent0")[1]);
			System.out.println("Average Score Rank of True1: "+g.AverageScore("Agent1")[0] + "             | " + g.AverageScore("Agent1")[1]);

			
			System.out.println("----------------Cutting Outlinks w/Sybils----------------------");
			System.out.println("PageRank of True0: "+gsybil.PageRank("Agent0")[0] + "                        | " + gsybil.PageRank("Agent0")[1]);
			System.out.println("PageRank of True1: "+gsybil.PageRank("Agent1")[0] + "                        | " + gsybil.PageRank("Agent1")[1]);

			System.out.println("Personalized PageRank of True0: "+gsybil.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[0] + "           | " + gsybil.PersonalizedPageRank("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized PageRank of True1: "+gsybil.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[0] + "           | " + gsybil.PersonalizedPageRank("Agent1",PublicVariable.getNagents())[1]);

			System.out.println("Global Hitting time Rank of True0: "+gsybil.GlobalHittingTime("Agent0")[0] + "        | " + gsybil.GlobalHittingTime("Agent0")[1]);
			System.out.println("Global Hitting time Rank of True1: "+gsybil.GlobalHittingTime("Agent1")[0] + "        | " + gsybil.GlobalHittingTime("Agent1")[1]);

			System.out.println("Personalized Hitting Time Rank of True0: "+gsybil.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[0] + " | " + gsybil.PersonalizedHittingTime("Agent0",PublicVariable.getNagents())[1]);
			System.out.println("Personalized Hitting Time Rank of True1: "+gsybil.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[0] + " | " + gsybil.PersonalizedHittingTime("Agent1",PublicVariable.getNagents())[1]);

			gsybil.reportZero("Agent0");
			gsybil.reportZero("Agent1");
			gsybil.SetTransitionMatrix();
			System.out.println("Average Score Rank of True0: "+gsybil.AverageScore("Agent0")[0] + "              | " + gsybil.AverageScore("Agent0")[1]);
			System.out.println("Average Score Rank of True1: "+gsybil.AverageScore("Agent1")[0] + "              | " + gsybil.AverageScore("Agent1")[1]);

		}
		
		protected void handleAllResultNotifications(Vector resultNotifications) {
			System.out.println("got " + resultNotifications.size() + " result notifs!");
		}
		
	}



}
