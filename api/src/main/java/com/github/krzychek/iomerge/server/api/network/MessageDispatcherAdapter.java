package com.github.krzychek.iomerge.server.api.network;

import com.github.krzychek.iomerge.server.api.AbstractChainable;
import com.github.krzychek.iomerge.server.model.Edge;
import com.github.krzychek.iomerge.server.model.message.Message;


/**
 * adapter for {@link MessageDispatcher} with all method delegating to chained objects
 */
public abstract class MessageDispatcherAdapter extends AbstractChainable<MessageDispatcher> implements MessageDispatcher {

	public void dispatchMouseMove(int x, int y) {
		nextInChain.dispatchMouseMove(x, y);
	}

	public void dispatchMousePress() {
		nextInChain.dispatchMousePress();
	}

	public void dispatchMouseRelease() {
		nextInChain.dispatchMouseRelease();
	}

	public void dispatchKeyPress(int keyCode) {
		nextInChain.dispatchKeyPress(keyCode);
	}

	public void dispatchKeyRelease(int keyCode) {
		nextInChain.dispatchKeyRelease(keyCode);
	}

	public void dispatchMouseWheelEvent(int wheelRotation) {
		nextInChain.dispatchMouseWheelEvent(wheelRotation);
	}

	public void dispatchClipboardSync(String msg) {
		nextInChain.dispatchClipboardSync(msg);
	}

	public void dispatchEdgeSync(Edge edge) {
		nextInChain.dispatchEdgeSync(edge);
	}

	public void dispatchCustomMsg(Message msg) {
		nextInChain.dispatchCustomMsg(msg);
	}

	public void dispatchKeyClick(int keyCode) {
		nextInChain.dispatchKeyClick(keyCode);
	}
}
