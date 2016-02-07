package pl.kbieron.iomerge.server.appState;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.util.Set;


class AppStateHolder implements AppStateManager {

	private final Logger log = Logger.getLogger(AppStateHolder.class);

	private AppState state;

	private Set<AppStateListener> listeners;

	@Inject
	AppStateHolder(Set<AppStateListener> listeners) {
		this.listeners = listeners;
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
