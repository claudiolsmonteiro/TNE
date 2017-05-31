package Agents;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import Agents.ReputationAgent.FIPAContractNetInit;
import Agents.TrueAgent.FIPAContractNetResp;
import Graph.Graph;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

public class SybilAgent extends Agent {
	String father;
	public SybilAgent(String f) {
		// TODO Auto-generated constructor stub
		super();
		this.father = f;
	}

	public void setup() {
		addBehaviour(new FIPAContractNetResp(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
		
	}
	
	class FIPAContractNetResp extends ContractNetResponder {
		public FIPAContractNetResp(Agent a, MessageTemplate mt) {
			super(a,mt);
		}
		
		protected ACLMessage handleCfp(ACLMessage cfp) {
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			String resp = ResponseContent(100);
			reply.setContent(resp);

			//this.myAgent.addBehaviour(new FIPAContractNetResp(this.myAgent, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
			return reply;
		}
		private String ResponseContent(int n) {
			
			String message = "";
			
			for(int i = 0; i < n; i++){
				if(father.equals("Agent"+i)){
					double f = (double)1.0;
					message = message + f + "|";
				}
				else{
					message = message + "-" +"|";
				}
			}
			return message;
		}
	}
}
