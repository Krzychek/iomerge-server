package com.github.krzychek.server.api.appState;

import com.github.krzychek.server.api.Chainable;


/**
 * Menages {@link AppState} in the application
 *
 * @see Chainable
 */
public interface AppStateManager extends Chainable<AppStateManager> {

	void enterRemoteScreen(MouseRestoreListener edgeTrigger);

	void restoreMouse();

	void connected();

	void returnToLocal(Float position);

	void disconnected();
}
