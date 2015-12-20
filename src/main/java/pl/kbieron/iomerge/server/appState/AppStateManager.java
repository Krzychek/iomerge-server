package pl.kbieron.iomerge.server.appState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Observable;


@Component
public class AppStateManager extends Observable {

	private final Log log = LogFactory.getLog(AppStateManager.class);

	private StateType stateChange;

	public synchronized void enterRemoteScreen() {
		setNewState(StateType.ON_REMOTE);
	}

	public synchronized void exitRemote() {
		setNewState(StateType.ON_LOCAL);
	}

	public synchronized StateType getStateChange() {
		return stateChange;
	}

	public synchronized void connected() {
		setNewState(StateType.ON_LOCAL);
	}

	public synchronized void disconnected() {
		setNewState(StateType.DISCONNECTED);
	}

	private void setNewState(StateType newState) {
		if ( stateChange != newState ) {
			log.info("state change:" + newState);
			stateChange = newState;
			setChanged();
			notifyObservers();
		}
	}
}
