package pl.kbieron.iomerge.server.api.appState;

import pl.kbieron.iomerge.server.api.ChainableAdapter;


public abstract class AppStateManagerAdapter extends ChainableAdapter<AppStateManager> implements AppStateManager {

	public void enterRemoteScreen() {
		nextInChain.enterRemoteScreen();
	}

	public void exitRemote() {
		nextInChain.exitRemote();
	}

	public void connected() {
		nextInChain.connected();
	}

	public void disconnected() {
		nextInChain.disconnected();
	}
}
