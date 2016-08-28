package com.github.krzychek.iomerge.server.api.appState;

import com.github.krzychek.iomerge.server.api.Chainable;


/**
 * Menages {@link AppState} in the application
 *
 * @see Chainable
 */
public interface AppStateManager {

	void enterRemoteScreen(MouseRestoreListener edgeTrigger);

	void restoreMouse();

	void connected();

	void returnToLocal(Float position);

	void disconnected();
}
