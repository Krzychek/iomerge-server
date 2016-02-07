package pl.kbieron.iomerge.server.appState;


public interface AppStateManager {

	void enterRemoteScreen();

	void exitRemote();

	void connected();

	void disconnected();
}
