package com.github.krzychek.iomerge.server.api.appState;


/**
 * Defines global state of application
 * <p>
 * Components what listen to global {@link AppState} change events should annotate method with one parameter of type {@link AppState} with
 * {@link org.springframework.context.event.EventListener}
 */
public enum AppState {
	ON_REMOTE,
	ON_LOCAL,
	DISCONNECTED,
	STARTUP
}