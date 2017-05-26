package Agents;

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
	public void sendMessage() {
		this.addBehaviour(new FIPAContractNetInit(this, new ACLMessage(ACLMessage.CFP)));
	}
	
	class FIPAContractNetResp extends ContractNetResponder {
		public FIPAContractNetResp(Agent a, MessageTemplate mt) {
			super(a,mt);
		}
		
		protected ACLMessage handleCfp(ACLMessage cfp) {
			/*ACLMessage reply = cfp.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			reply.setContent("I will do it for free!!!");
			*/
			System.out.println(cfp.getContent());
			//return reply;
			return null;
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
	class FIPAContractNetInit extends ContractNetInitiator {

		public FIPAContractNetInit(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		protected Vector prepareCfps(ACLMessage cfp) {
			Vector v = new Vector();
			
			for(int i = 0; i < 50; i++) {
				if(!("True"+i).equals(myAgent.getLocalName())) {
					cfp.addReceiver(new AID("True"+i, false));
					cfp.setContent(myAgent.getLocalName());

					v.add(cfp);
				}
			}
			
			return v;
		}

		protected void handleAllResponses(Vector responses, Vector acceptances) {
			
			System.out.println("got " + responses.size() + " responses!");
			
			for(int i=0; i<responses.size(); i++) {
				/*ACLMessage msg = ((ACLMessage) responses.get(i)).createReply();
				msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL); // OR NOT!
				acceptances.add(msg);*/
				System.out.println("Received Response from: "+responses.get(i).toString());
			}
		}
		
		protected void handleAllResultNotifications(Vector resultNotifications) {
			System.out.println("got " + resultNotifications.size() + " result notifs!");
		}
		
	}
}
