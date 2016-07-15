package com.github.krzychek.server.api.appState;

import com.github.krzychek.server.api.Chainable;


/**
 * Menages {@link AppState} in the application
 *
 * @see Chainable
 */
public interface AppStateManager extends Chainable<AppStateManager> {

	void enterRemoteScreen();

	void exitRemote();

	void connected();

	void disconnected();
}
