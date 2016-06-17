package pl.kbieron.iomerge.server.appState;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;


/**
 * Holds state of application, and emit its changes t given listeners
 */
@Component
class AppStateHolder implements AppStateManager {

	private final Logger log = Logger.getLogger(AppStateHolder.class);

	private AppState state;

	@Autowired
	private Set<AppStateListener> listeners;

	@PostConstruct
	void init() {
		setNewState(AppState.DISCONNECTED);
	}

	@Override
	public void enterRemoteScreen() {
		setNewState(AppState.ON_REMOTE);
	}

	@Override
	public void exitRemote() {
		setNewState(AppState.ON_LOCAL);
	}

	@Override
	public void connected() {
		setNewState(AppState.ON_LOCAL);
	}

	@Override
	public void disconnected() {
		setNewState(AppState.DISCONNECTED);
	}

	private synchronized void setNewState(AppState newState) {
		if ( state != newState ) {
			log.info("state: " + newState);
			state = newState;
			listeners.forEach(listener -> listener.onStateChange(newState));
		}
	}
}
