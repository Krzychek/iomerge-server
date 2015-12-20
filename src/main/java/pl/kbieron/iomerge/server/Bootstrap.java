package pl.kbieron.iomerge.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap {

	public static void main(String... args) throws InterruptedException {
		new ClassPathXmlApplicationContext("/spring.xml");
	}
}
