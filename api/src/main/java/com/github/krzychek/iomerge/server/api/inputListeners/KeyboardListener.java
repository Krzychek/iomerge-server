package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.Chainable;


public interface KeyboardListener extends Chainable<KeyboardListener> {

	void keyTyped(int keyCode);

	void keyPressed(int keyCode);

	void keyReleased(int keyCode);
}
