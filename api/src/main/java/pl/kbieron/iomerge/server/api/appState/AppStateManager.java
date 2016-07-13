package pl.kbieron.iomerge.server.api.appState;

import pl.kbieron.iomerge.server.api.Chainable;


/**
 * Menages {@link AppState}
 */
public interface AppStateManager extends Chainable<AppStateManager> {

	void enterRemoteScreen();

	void exitRemote();

	void connected();

	void disconnected();
}
