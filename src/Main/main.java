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
		
		AgentController ac1;
		try {
			for(int i = 0; i< 50; i++) {
				ac1 = mainContainer.acceptNewAgent("True"+i, new TrueAgent());
				System.out.println("True"+1);
				ac1.start();
			}

		}
		catch( StaleProxyException e) {
			e.printStackTrace();
		}
	mainContainer.getAgent("True1").;
	}
}
