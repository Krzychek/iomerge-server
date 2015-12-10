package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;
import pl.kbieron.iomerge.server.ui.movementReader.MovementReader;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.swing.Timer;


@Component
public class Director {

	private final Timer delayedTriggerStarer;

	@Autowired
	private VirtualScreen virtualScreen;

	@Autowired
	private MovementReader movementReader;

	@Autowired
	private EdgeTrigger edgeTrigger;

	public Director() {
		delayedTriggerStarer = new Timer(500, this::start);
		delayedTriggerStarer.setRepeats(false);
	}

	public void enterRemoteScreen(double enterPosition) {
		edgeTrigger.setVisible(false);
		virtualScreen.enter(enterPosition, Edge.LEFT);
		movementReader.startReading();
	}

	public void start(Object... args) {
		edgeTrigger.reposition();
		edgeTrigger.setVisible(true);
	}

	public void exitRemote() {
		movementReader.stopReading();
		delayedTriggerStarer.restart();
	}
}
