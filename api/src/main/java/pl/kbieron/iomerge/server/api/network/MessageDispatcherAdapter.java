package pl.kbieron.iomerge.server.api.network;

import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.server.api.AbstractChainable;


/**
 * adapter for {@link MessageDispatcher} with all method delegating to chained objects
 */
public abstract class MessageDispatcherAdapter extends AbstractChainable<MessageDispatcher> implements MessageDispatcher {

	public void dispatchMouseSync(int x, int y) {
		nextInChain.dispatchMouseSync(x, y);
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
