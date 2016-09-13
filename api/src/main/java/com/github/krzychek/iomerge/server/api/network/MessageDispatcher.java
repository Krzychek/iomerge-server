package com.github.krzychek.iomerge.server.api.network;

import com.github.krzychek.iomerge.server.api.Chainable;
import com.github.krzychek.iomerge.server.model.Edge;
import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton;


/**
 * Implementation dispatches messages to client by proper client handler
 *
 * @see Chainable
 */
public interface MessageDispatcher extends Chainable<MessageDispatcher> {

	void dispatchMouseMove(int x, int y);

	void dispatchMousePress(MouseButton button);

	void dispatchMouseRelease(MouseButton button);

	void dispatchKeyPress(int keyCode);

	void dispatchKeyRelease(int keyCode);

	void dispatchMouseWheelEvent(int wheelRotation);

	void dispatchClipboardSync(String msg);

	void dispatchEdgeSync(Edge edge);

	void dispatchCustomMsg(Message msg);

	void dispatchKeyClick(int keyCode);
}
