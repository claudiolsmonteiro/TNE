package Main;

import Agents.*;
import PublicVar.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class main {

	public static void main(String [] args) {
		Runtime rt = Runtime.instance();
		
		Profile p1 = new ProfileImpl();
		ContainerController mainContainer = rt.createMainContainer(p1);
		
		AgentController trueAgents;
		try {
			trueAgents = mainContainer.acceptNewAgent("Agent0", new TrueAgent(PublicVariable.getNsybils(),0));
			trueAgents.start();
			trueAgents = mainContainer.acceptNewAgent("Agent1", new TrueAgent((int)Math.ceil((double)PublicVariable.getNsybils()*0.75),PublicVariable.getNsybils()));
			trueAgents.start();
			for(int i = 2; i< PublicVariable.getNagents(); i++) {
				trueAgents = mainContainer.acceptNewAgent("Agent"+i, new TrueAgent(0,0));
				trueAgents.start();
			}
		}
		catch( StaleProxyException e) {
			e.printStackTrace();
		}
		
		
		AgentController reputation;
		try {
			reputation = mainContainer.acceptNewAgent("Reputation", new ReputationAgent());
			reputation.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
