package pl.kbieron.iomerge.server.appState;

import org.pmw.tinylog.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.appState.AppState;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;


/**
 * Holds state of application, and publish state events on change
 */
@Order(0)
@Component
class AppStateHolder implements AppStateManager {

	private final ApplicationEventPublisher publisher;

	private AppState state;
	private AppStateManager nextInChain;

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
		nextInChain.enterRemoteScreen();
	}

	@Override
	public void exitRemote() {
		setNewState(AppState.ON_LOCAL);
		nextInChain.exitRemote();
	}

	@Override
	public void connected() {
		setNewState(AppState.ON_LOCAL);
		nextInChain.connected();
	}

	@Override
	public void disconnected() {
		setNewState(AppState.DISCONNECTED);
		nextInChain.disconnected();
	}

	private synchronized void setNewState(AppState newState) {
		if (state != newState) {
			Logger.info("setting application state to: " + newState);
			state = newState;
			publisher.publishEvent(state);
		}
	}

	@Override
	public void chain(AppStateManager nextInChain) {
		this.nextInChain = nextInChain;
	}
}