package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.AbstractChainable;


/**
 * adapter for {@link KeyboardListener} with all method delegating to chained objects
 */
public abstract class KeyboardListenerAdapter extends AbstractChainable<KeyboardListener> implements KeyboardListener {

	@Override
	public void keyTyped(int keyCode) {
		nextInChain.keyTyped(keyCode);
	}

	@Override
	public void keyPressed(int keyCode) {
		nextInChain.keyPressed(keyCode);
	}

	@Override
	public void keyReleased(int keyCode) {
		nextInChain.keyReleased(keyCode);
	}
}
