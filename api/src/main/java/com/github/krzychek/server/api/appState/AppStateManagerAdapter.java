package com.github.krzychek.server.api.appState;

import com.github.krzychek.server.api.AbstractChainable;


/**
 * adapter for {@link AppStateManager} with all method delegating to chained objects
 */
public abstract class AppStateManagerAdapter extends AbstractChainable<AppStateManager> implements AppStateManager {

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
