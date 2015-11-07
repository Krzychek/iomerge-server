package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;

import java.util.function.DoubleConsumer;


@Component
public class Director {

	@Autowired
	private VirtualScreen virtualScreen;

	private DoubleConsumer exitCallback;

	public void enterRemoteScreen(double enterPosition, DoubleConsumer exitCallback) {
		this.exitCallback = exitCallback;
		virtualScreen.enter(enterPosition);

	}

	public void exitRemote(double exitPosition) {
		if ( exitCallback != null ) exitCallback.accept(exitPosition);

	}
}
