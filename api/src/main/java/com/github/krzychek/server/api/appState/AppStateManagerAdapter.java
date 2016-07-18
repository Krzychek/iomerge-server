package com.github.krzychek.server.api.appState;

import com.github.krzychek.server.api.AbstractChainable;


/**
 * adapter for {@link AppStateManager} with all method delegating to chained objects
 */
public abstract class AppStateManagerAdapter extends AbstractChainable<AppStateManager> implements AppStateManager {

	@Override
	public void enterRemoteScreen(MouseRestoreListener mouseRestoreListener) {
		nextInChain.enterRemoteScreen(mouseRestoreListener);
	}

	@Override
	public void restoreMouse() {
		nextInChain.restoreMouse();
	}

	@Override
	public void connected() {
		nextInChain.connected();
	}

	@Override
	public void disconnected() {
		nextInChain.disconnected();
	}

	@Override
	public void returnToLocal(Float position) {
		nextInChain.returnToLocal(position);

	}
}
