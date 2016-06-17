package pl.kbieron.iomerge.server.appState;

/**
 * Menages {@link AppState}
 */
public interface AppStateManager {

	void enterRemoteScreen();

	void exitRemote();

	void connected();

	void disconnected();
}
