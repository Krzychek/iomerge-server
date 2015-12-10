package pl.kbieron.iomerge.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.kbieron.iomerge.server.network.RMIServer;


public class Bootstrap {

	public static void main(String... args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");

		RMIServer object = context.getBean(RMIServer.class);

		while ( true ) {
			object.hitBackBtn();
			Thread.sleep(1000);
		}
	}
}
