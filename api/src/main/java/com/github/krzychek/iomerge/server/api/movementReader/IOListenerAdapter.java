package com.github.krzychek.iomerge.server.api.movementReader;

import com.github.krzychek.iomerge.server.api.AbstractChainable;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
 * adapter for {@link IOListener} with all method delegating to chained objects
 */
public abstract class IOListenerAdapter extends AbstractChainable<IOListener> implements IOListener {

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

	@Override
	public void mouseClicked(MouseEvent e) {
		nextInChain.mouseClicked(e);
	}
}
