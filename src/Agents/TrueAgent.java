package Agents;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import PublicVar.PublicVariable;
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
	int mySybils, sybilIndex;
	public void setup() {
		addSybils(mySybils);
		addBehaviour(new FIPAContractNetResp(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
	}
	
	public TrueAgent (int mySybils, int sybilIndex) {
		super();
		this.mySybils = mySybils;
		this.sybilIndex = sybilIndex;

	}
	
	public void addSybils(int s) {
		// TODO Auto-generated method stub
		ContainerController cc = this.getContainerController();
		for(int i = sybilIndex; i < sybilIndex+mySybils; i++) {
			AgentController ac;
			try {
				System.out.println("Agent: "+this.getLocalName());
				System.out.println("sybil index:"+i);
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
			String resp = ResponseContent(PublicVariable.getNagents());
			reply.setContent(resp);

			//this.myAgent.addBehaviour(new FIPAContractNetResp(this.myAgent, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
			return reply;
		}
		private String ResponseContent(int n) {
			
			String message = "";
			Random rand = new Random();
			
			for(int i = 0; i < n; i++){
				Integer r = rand.nextInt(10); //have 1/10 th probability of not having a rating
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
