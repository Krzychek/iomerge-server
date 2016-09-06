package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.AbstractChainable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
 * adapter for {@link MouseListener} with all method delegating to chained objects
 */
public abstract class MouseListenerAdapter extends AbstractChainable<MouseListener> implements MouseListener {

	@Override
	public void mousePressed(MouseEvent e) {
		nextInChain.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		nextInChain.mouseReleased(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		nextInChain.mouseWheelMoved(e);
	}

	@Override
	public void move(int dx, int dy) {
		nextInChain.move(dx, dy);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		nextInChain.mouseClicked(e);
	}
}
