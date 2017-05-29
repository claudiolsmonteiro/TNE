package Agents;

import java.util.Random;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

public class TrueAgent extends Agent {
	
	public void setup() {
		addBehaviour(new FIPAContractNetResp(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
	}
	
	//-------------------------------------------FIPA--------------------------------------
	class FIPAContractNetResp extends ContractNetResponder {
		public FIPAContractNetResp(Agent a, MessageTemplate mt) {
			super(a,mt);
		}
		
		protected ACLMessage handleCfp(ACLMessage cfp) {
			
			ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			String resp = ResponseContent(50);
			reply.setContent(resp);
			return reply;
		}
		private String ResponseContent(int n) {
			
			String message = "";
			Random rand = new Random();
			
			for(int i = 0; i < 250; i++){
				Integer r = rand.nextInt(5); //have 1/5 th probability of not having a rating
				if(r>0){
					Float f = rand.nextFloat();
					message = message + f + "|";
				}
				else{
					message = message + "-" +"|";
				}
			}
			return message;
		}
		/*
		protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
			System.out.println(myAgent.getLocalName() + " got a reject...");
		}

		protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
			System.out.println(myAgent.getLocalName() + " got an accept!");
			ACLMessage result = accept.createReply();
			result.setPerformative(ACLMessage.INFORM);
			result.setContent("this is the result");
			
			return result;
		}*/
	}
}
