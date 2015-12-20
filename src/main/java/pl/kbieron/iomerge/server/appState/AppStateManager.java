package pl.kbieron.iomerge.server.appState;

import org.springframework.stereotype.Component;

import java.util.Observable;


@Component
public class AppStateManager extends Observable {

	private StateType stateChange;

	private double position;

	public synchronized void enterRemoteScreen(double enterPosition) {
		this.position = enterPosition;
		setNewState(StateType.ON_REMOTE);
	}

	public synchronized void exitRemote() {
		setNewState(StateType.CONNECTED);
	}

	public synchronized StateType getStateChange() {
		return stateChange;
	}

	public synchronized double getPosition() {
		return position;
	}

	public synchronized void connected() {
		setNewState(StateType.CONNECTED);
	}

	public synchronized void disconnected() {
		setNewState(StateType.DISCONNECTED);
	}

	private void setNewState(StateType newState) {
		if ( stateChange != newState ) {
			stateChange = newState;
			setChanged();
			notifyObservers();
		}
	}
}
