package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.Chainable;

import java.awt.event.KeyEvent;


public interface KeyboardListener extends Chainable<KeyboardListener> {

	void keyTyped(KeyEvent e);

	void keyPressed(KeyEvent e);

	void keyReleased(KeyEvent e);
}
