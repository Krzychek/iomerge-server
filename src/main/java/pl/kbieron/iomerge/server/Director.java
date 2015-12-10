package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;
import pl.kbieron.iomerge.server.ui.movementReader.MovementReader;

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
		delayedTriggerStarer = new Timer(1000, actionEvent -> {
			edgeTrigger.setVisible(true);
			edgeTrigger.reposition();
		});
		delayedTriggerStarer.setRepeats(false);
	}

	public void enterRemoteScreen(double enterPosition) {
		virtualScreen.enter(enterPosition);
		movementReader.startReading();
		edgeTrigger.setVisible(false);

	}

	public void exitRemote(double exitPosition) {
		movementReader.stopReading();
		delayedTriggerStarer.restart();
	}
}
