package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;
import pl.kbieron.iomerge.server.ui.movementReader.MouseTrapReader;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;


@Component
public class Bootstrap {

	@Autowired
	EdgeTrigger edgeTrigger;

	@Autowired
	VirtualScreen virtualScreen;

	@Autowired
	MouseTrapReader mouseTrapReader;

	@Autowired
	private AppStateManager appStateManager;

	public static void main(String... args) throws InterruptedException {
		new ClassPathXmlApplicationContext("/spring.xml");
	}

	@PostConstruct
	void init() {
		edgeTrigger.setEdge(Edge.RIGHT);

		appStateManager.addObservers(mouseTrapReader, edgeTrigger, virtualScreen);
	}
}
