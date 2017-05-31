package Agents;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class TrueAgent extends Agent {
	int s;
	public void setup() {
		addSybils(s);
		addBehaviour(new FIPAContractNetResp(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
	}
	
	public TrueAgent (int s) {
		super();
		this.s = s;

	}
	
	public void addSybils(int s) {
		// TODO Auto-generated method stub
		ContainerController cc = this.getContainerController();
		for(int i = 0; i < s; i++) {
			AgentController ac;
			try {
				ac = cc.acceptNewAgent("Sybil"+i, new SybilAgent(this.getLocalName()));	
				ac.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	//-------------------------------------------FIPA--------------------------------------
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
			Random rand = new Random();
			
			for(int i = 0; i < n; i++){
				Integer r = rand.nextInt(4); //have 1/4 th probability of not having a rating
				if(r>0){
					double f = rand.nextDouble();
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
