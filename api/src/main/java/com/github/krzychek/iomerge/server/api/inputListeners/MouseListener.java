package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.Chainable;
import com.github.krzychek.iomerge.server.model.MouseButton;


/**
 * Receives mouse input application events
 *
 * @see Chainable
 */
public interface MouseListener extends Chainable<MouseListener> {

	void move(int dx, int dy);

	void mouseClicked(MouseButton button);

	void mousePressed(MouseButton button);

	void mouseReleased(MouseButton button);

	void mouseWheelMoved(int wheelRotation);

}
