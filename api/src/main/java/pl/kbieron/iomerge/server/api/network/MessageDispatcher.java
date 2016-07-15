package pl.kbieron.iomerge.server.api.network;

import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.server.api.Chainable;


/**
 * Implementation dispatches messages to client by proper client handler
 *
 * @see Chainable
 */
public interface MessageDispatcher extends Chainable<MessageDispatcher> {

	void dispatchMouseSync(int x, int y);

	void dispatchMousePress();

	void dispatchMouseRelease();

	void dispatchKeyPress(int keyCode);

	void dispatchKeyRelease(int keyCode);

	void dispatchMouseWheelEvent(int wheelRotation);

	void dispatchClipboardSync(String msg);

	void dispatchEdgeSync(Edge edge);

	void dispatchCustomMsg(Message msg);

	void dispatchKeyClick(int keyCode);
}
