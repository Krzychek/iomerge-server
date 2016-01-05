package pl.kbieron.iomerge.server.appState;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;


public class AppStateManager implements ApplicationEventPublisherAware {

	private final Logger log = Logger.getLogger(AppStateManager.class);

	private AppState state;

	private ApplicationEventPublisher eventPublisher;

	public synchronized void enterRemoteScreen() {
		setNewState(AppState.ON_REMOTE);
	}

	public synchronized void exitRemote() {
		setNewState(AppState.ON_LOCAL);
	}

	public synchronized void connected() {
		setNewState(AppState.ON_LOCAL);
	}

	public synchronized void disconnected() {
		setNewState(AppState.DISCONNECTED);
	}

	private void setNewState(AppState newState) {
		if ( state != newState ) {
			log.info("state: " + newState);
			state = newState;
			eventPublisher.publishEvent(newState.getUpdateEvent());
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		eventPublisher = applicationEventPublisher;
	}
}
