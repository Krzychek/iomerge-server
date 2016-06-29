package pl.kbieron.iomerge.server.appState;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Holds state of application, and publish state events on change
 */
@Component
class AppStateHolder implements AppStateManager {

	private final static Logger log = Logger.getLogger(AppStateHolder.class);

	private final ApplicationEventPublisher publisher;

	private AppState state;

	AppStateHolder(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@EventListener(ContextRefreshedEvent.class)
	private synchronized void onContextRefreshed() {
		if (state == null) {
			setNewState(AppState.DISCONNECTED);
		}
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
		if (state != newState) {
			log.info("setting application state to: " + newState);
			state = newState;
			publisher.publishEvent(state);
		}
	}
}
