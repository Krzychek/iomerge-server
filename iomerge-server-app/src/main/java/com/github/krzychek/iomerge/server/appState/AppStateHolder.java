package com.github.krzychek.iomerge.server.appState;

import com.github.krzychek.iomerge.server.api.appState.AppState;
import com.github.krzychek.iomerge.server.api.appState.AppStateManagerAdapter;
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


/**
 * Holds state of application, and publish state events on change
 */
@Order(0)
@Component
class AppStateHolder extends AppStateManagerAdapter {

	private final ApplicationEventPublisher publisher;
	private final Set<MouseRestoreListener> mouseRestoreListeners;

	private AppState state;
	private Float position;

	@Autowired
	AppStateHolder(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		mouseRestoreListeners = new HashSet<>();
	}

	@EventListener(ContextRefreshedEvent.class)
	private synchronized void onContextRefreshed() {
		if (state == null) {
			setNewState(AppState.DISCONNECTED);
		}
	}

	@Override
	public void enterRemoteScreen(MouseRestoreListener mouseRestoreListener) {
		mouseRestoreListeners.add(mouseRestoreListener);
		setNewState(AppState.ON_REMOTE);
		nextInChain.enterRemoteScreen(mouseRestoreListener);
	}

	@Override
	public void restoreMouse() {
		if (position != null)
			mouseRestoreListeners.forEach(listener -> listener.restoreMouseAt(position));

		mouseRestoreListeners.clear();
		position = null;

		nextInChain.restoreMouse();
	}

	@Override
	public void connected() {
		setNewState(AppState.ON_LOCAL);
		nextInChain.connected();
	}

	@Override
	public void returnToLocal(Float position) {
		this.position = position;
		setNewState(AppState.ON_LOCAL);

		nextInChain.returnToLocal(position);
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
}
