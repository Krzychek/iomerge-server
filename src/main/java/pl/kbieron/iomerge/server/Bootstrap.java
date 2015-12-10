package pl.kbieron.iomerge.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.kbieron.iomerge.server.network.RMIServer;
import pl.kbieron.iomerge.server.ui.movementReader.MouseTrapReader;


public class Bootstrap {

	public static void main(String... args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");

		RMIServer object = context.getBean(RMIServer.class);

		MouseTrapReader mouseTrapReader = context.getBean(MouseTrapReader.class);

		mouseTrapReader.startReading();
	}
}
