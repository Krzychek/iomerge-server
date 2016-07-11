package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.keyboard.KeyClick;
import pl.kbieron.iomerge.model.message.keyboard.KeyPress;
import pl.kbieron.iomerge.model.message.keyboard.KeyRelease;
import pl.kbieron.iomerge.model.message.misc.ClipboardSync;
import pl.kbieron.iomerge.model.message.misc.EdgeSync;
import pl.kbieron.iomerge.model.message.mouse.MousePress;
import pl.kbieron.iomerge.model.message.mouse.MouseRelease;
import pl.kbieron.iomerge.model.message.mouse.MouseSync;
import pl.kbieron.iomerge.model.message.mouse.MouseWheel;
import pl.kbieron.iomerge.server.api.network.MessageDispatcher;


@Component
public class MessageDispatcherImpl implements MessageDispatcher {

	@Autowired
	private ConnectionHandler connectionHandler;

	@Override
	public void dispatchMouseSync(int x, int y) {
		connectionHandler.sendToClient(new MouseSync(x, y));
	}

	@Override
	public void dispatchMousePress() {
		connectionHandler.sendToClient(new MousePress());
	}

	@Override
	public void dispatchMouseRelease() {
		connectionHandler.sendToClient(new MouseRelease());
	}

	@Override
	public void dispatchKeyPress(int keyCode) {
		connectionHandler.sendToClient(new KeyPress(keyCode));
	}

	@Override
	public void dispatchKeyRelease(int keyCode) {
		connectionHandler.sendToClient(new KeyRelease(keyCode));
	}

	@Override
	public void dispatchMouseWheelEvent(int wheelRotation) {
		connectionHandler.sendToClient(new MouseWheel(wheelRotation));
	}

	@Override
	public void dispatchClipboardSync(String msg) {
		connectionHandler.sendToClient(new ClipboardSync(msg));
	}

	@Override
	public void dispatchEdgeSync(Edge edge) {
		connectionHandler.sendToClient(new EdgeSync(edge));
	}

	@Override
	public void dispatchCustomMsg(Message msg) {
		connectionHandler.sendToClient(msg);
	}

	@Override
	public void dispatchKeyClick(int keyCode) {
		connectionHandler.sendToClient(new KeyClick(keyCode));
	}
}
