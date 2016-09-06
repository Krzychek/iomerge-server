package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.Chainable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
 * Receives mouse input application events
 *
 * @see Chainable
 */
public interface MouseListener extends Chainable<MouseListener> {

	void move(int dx, int dy);

	void mouseClicked(MouseEvent e);

	void mousePressed(MouseEvent e);

	void mouseReleased(MouseEvent e);

	void mouseWheelMoved(MouseWheelEvent e);

}
