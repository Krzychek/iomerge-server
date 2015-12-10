package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.ui.InvisibleJWindow;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;
import java.awt.Color;


@Component
public class Bootstrap {

	@Autowired
	InvisibleJWindow edgeTrigger;

	@Autowired
	private Director director;

	public static void main(String... args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");
	}

	@PostConstruct
	void init() {
		edgeTrigger.setEdge(Edge.RIGHT);

		edgeTrigger.setBackground(Color.BLACK);
		director.start();
	}
}
