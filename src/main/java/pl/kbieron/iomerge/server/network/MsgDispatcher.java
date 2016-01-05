package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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


@Component
public class MsgDispatcher {

	@Autowired
	EventServer eventServer;

	public void dispatchMouseSync(short x, short y) {
		eventServer.sendToClient(new MouseSync(x, y));
	}

	public void dispatchMousePress() {
		eventServer.sendToClient(new MousePress());
	}

	public void dispatchMouseRelease() {
		eventServer.sendToClient(new MouseRelease());
	}

	public void dispatchKeyPress(int keyCode) {
		eventServer.sendToClient(new KeyPress(keyCode));
	}

	public void dispatchKeyRelease(int keyCode) {
		eventServer.sendToClient(new KeyRelease(keyCode));
	}

	public void dispatchMouseWheelEvent(int wheelRotation) {
		eventServer.sendToClient(new MouseWheel(wheelRotation));
	}

	public void dispatchClipboardSync(String msg) {
		eventServer.sendToClient(new ClipboardSync(msg));
	}

	public void dispatchEdgeSync(Edge edge) {
		eventServer.sendToClient(new EdgeSync(edge));
	}

	public void dispatchCustomMsg(Message msg) {
		eventServer.sendToClient(msg);
	}
}
