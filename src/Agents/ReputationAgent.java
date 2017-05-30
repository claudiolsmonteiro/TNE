package Agents;

import java.util.StringTokenizer;
import java.util.Vector;
import Graph.Graph;
import Agents.TrueAgent.FIPAContractNetResp;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;

public class ReputationAgent extends Agent {
	
	public Graph g;
	
	public void setup() {
		g = new Graph();
		addBehaviour(new FIPAContractNetInit(this, new ACLMessage(ACLMessage.CFP)));
	}
	
	
	//-------------------------------------------FIPA--------------------------------------

	class FIPAContractNetInit extends ContractNetInitiator {

		public FIPAContractNetInit(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		protected Vector prepareCfps(ACLMessage cfp) {
			Vector v = new Vector();
			
			for(int i = 0; i < 50; i++) {
					cfp.addReceiver(new AID("True"+i, false));
					g.AddNode("True"+i);
			}
			cfp.setContent(myAgent.getLocalName());

			v.add(cfp);
			return v;
		}

		protected void handleAllResponses(Vector responses, Vector acceptances) {
			
			System.out.println("got " + responses.size() + " responses!");
			
			for(int i=0; i<responses.size(); i++) {
				ACLMessage resp = ((ACLMessage) responses.get(i));
				//ACLMessage msg = ((ACLMessage) responses.get(i)).createReply();
				//msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL); // OR NOT!
				//acceptances.add(msg);
				
				String from = resp.getSender().getLocalName();
				StringTokenizer st = new StringTokenizer(resp.getContent(), "|");

				int t = 0;
				
				while(st.hasMoreTokens()){
					String to = "True"+t;
					String weight = st.nextToken();
					
					if(!weight.equals("-") && !from.equals(to)){
						g.AddEdge(from, to, Double.parseDouble(weight));
					}
					t++;
				}
			}
			g.SetTransitionMatrix(0.15);
			g.print();
			
			System.out.println("Average Score of True3: "+g.AverageScore("True3"));
			System.out.println("PageRank Score of True3: "+g.PageRank("True3"));
			System.out.println("-----------------------------------------------------");
			System.out.println("Average Score of True1: "+g.AverageScore("True1"));
			System.out.println("PageRank Score of True1: "+g.PageRank("True1"));
			System.out.println("-----------------------------------------------------");
			System.out.println("Average Score of True5: "+g.AverageScore("True5"));
			System.out.println("PageRank Score of True5: "+g.PageRank("True5"));
			System.out.println("-----------------------------------------------------");
			System.out.println("Average Score of True7: "+g.AverageScore("True7"));
			System.out.println("PageRank Score of True7: "+g.PageRank("True7"));
			
		}
		
		protected void handleAllResultNotifications(Vector resultNotifications) {
			System.out.println("got " + resultNotifications.size() + " result notifs!");
		}
		
	}
}
