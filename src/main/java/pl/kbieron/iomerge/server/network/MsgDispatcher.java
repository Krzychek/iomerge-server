package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.keyboard.KeyPress;
import pl.kbieron.iomerge.model.message.keyboard.KeyRelease;
import pl.kbieron.iomerge.model.message.misc.ClipboardSync;
import pl.kbieron.iomerge.model.message.misc.EdgeSync;
import pl.kbieron.iomerge.model.message.mouse.MousePress;
import pl.kbieron.iomerge.model.message.mouse.MouseRelease;
import pl.kbieron.iomerge.model.message.mouse.MouseSync;
import pl.kbieron.iomerge.model.message.mouse.MouseWheel;


public class MsgDispatcher {

	@Autowired
	private ConnectionHandler connectionHandler;

	public void dispatchMouseSync(short x, short y) {
		connectionHandler.sendToClient(new MouseSync(x, y));
	}

	public void dispatchMousePress() {
		connectionHandler.sendToClient(new MousePress());
	}

	public void dispatchMouseRelease() {
		connectionHandler.sendToClient(new MouseRelease());
	}

	public void dispatchKeyPress(int keyCode) {
		connectionHandler.sendToClient(new KeyPress(keyCode));
	}

	public void dispatchKeyRelease(int keyCode) {
		connectionHandler.sendToClient(new KeyRelease(keyCode));
	}

	public void dispatchMouseWheelEvent(int wheelRotation) {
		connectionHandler.sendToClient(new MouseWheel(wheelRotation));
	}

	public void dispatchClipboardSync(String msg) {
		connectionHandler.sendToClient(new ClipboardSync(msg));
	}

	public void dispatchEdgeSync(Edge edge) {
		connectionHandler.sendToClient(new EdgeSync(edge));
	}

	public void dispatchCustomMsg(Message msg) {
		connectionHandler.sendToClient(msg);
	}
}
