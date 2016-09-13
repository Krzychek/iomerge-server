package com.github.krzychek.iomerge.server.api.inputListeners;

import com.github.krzychek.iomerge.server.api.AbstractChainable;
import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton;


/**
 * adapter for {@link MouseListener} with all method delegating to chained objects
 */
public abstract class MouseListenerAdapter extends AbstractChainable<MouseListener> implements MouseListener {

	@Override
	public void mousePressed(MouseButton button) {
		nextInChain.mousePressed(button);
	}

	@Override
	public void mouseReleased(MouseButton button) {
		nextInChain.mouseReleased(button);
	}

	@Override
	public void mouseWheelMoved(int wheelRotation) {
		nextInChain.mouseWheelMoved(wheelRotation);
	}

	@Override
	public void move(int dx, int dy) {
		nextInChain.move(dx, dy);
	}

	@Override
	public void mouseClicked(MouseButton button) {
		nextInChain.mouseClicked(button);
	}
}
