package Main;

import Agents.*;
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
			for(int i = 0; i< 250; i++) {
				trueAgents = mainContainer.acceptNewAgent("True"+i, new TrueAgent());
				System.out.println("True"+i);
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
