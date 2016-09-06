package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.AbstractChainable;

import java.awt.event.KeyEvent;


/**
 * adapter for {@link KeyboardListener} with all method delegating to chained objects
 */
public abstract class KeyboardListenerAdapter extends AbstractChainable<KeyboardListener> implements KeyboardListener {

	@Override
	public void keyTyped(KeyEvent e) {
		nextInChain.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		nextInChain.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		nextInChain.keyReleased(e);
	}
}
