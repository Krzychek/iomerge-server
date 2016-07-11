package pl.kbieron.iomerge.server.api.appState;

/**
 * Menages {@link AppState}
 */
public interface AppStateManager {

	void enterRemoteScreen();

	void exitRemote();

	void connected();

	void disconnected();
}
