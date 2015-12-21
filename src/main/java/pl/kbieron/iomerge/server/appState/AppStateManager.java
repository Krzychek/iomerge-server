package pl.kbieron.iomerge.server.appState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class AppStateManager implements ApplicationEventPublisherAware {

	private final Log log = LogFactory.getLog(AppStateManager.class);

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
			log.info("state change:" + state + " to:" + newState);
			state = newState;
			eventPublisher.publishEvent(newState.getUpdateEvent());
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		eventPublisher = applicationEventPublisher;
	}
}
