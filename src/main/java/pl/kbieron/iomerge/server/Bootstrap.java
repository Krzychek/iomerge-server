package pl.kbieron.iomerge.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;


public class Bootstrap {

	public static void main(String... args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");

		EdgeTrigger trigger = context.getBean(EdgeTrigger.class);
		trigger.reposition();
	}
}
