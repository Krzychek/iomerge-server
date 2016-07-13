package pl.kbieron.iomerge.server.network;

import org.springframework.core.annotation.Order;
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


@Order(0)
@Component
public class MessageDispatcherImpl implements MessageDispatcher {

	private final ConnectionHandler connectionHandler;

	private MessageDispatcher nextInChain;

	public MessageDispatcherImpl(ConnectionHandler connectionHandler) {this.connectionHandler = connectionHandler;}

	@Override
	public void dispatchMouseSync(int x, int y) {
		connectionHandler.sendToClient(new MouseSync(x, y));
		nextInChain.dispatchMouseSync(x, y);
	}

	@Override
	public void dispatchMousePress() {
		connectionHandler.sendToClient(new MousePress());
		nextInChain.dispatchMousePress();
	}

	@Override
	public void dispatchMouseRelease() {
		connectionHandler.sendToClient(new MouseRelease());
		nextInChain.dispatchMouseRelease();
	}

	@Override
	public void dispatchKeyPress(int keyCode) {
		connectionHandler.sendToClient(new KeyPress(keyCode));
		nextInChain.dispatchKeyPress(keyCode);
	}

	@Override
	public void dispatchKeyRelease(int keyCode) {
		connectionHandler.sendToClient(new KeyRelease(keyCode));
		nextInChain.dispatchKeyRelease(keyCode);
	}

	@Override
	public void dispatchMouseWheelEvent(int wheelRotation) {
		connectionHandler.sendToClient(new MouseWheel(wheelRotation));
		nextInChain.dispatchMouseWheelEvent(wheelRotation);
	}

	@Override
	public void dispatchClipboardSync(String msg) {
		connectionHandler.sendToClient(new ClipboardSync(msg));
		nextInChain.dispatchClipboardSync(msg);
	}

	@Override
	public void dispatchEdgeSync(Edge edge) {
		connectionHandler.sendToClient(new EdgeSync(edge));
		nextInChain.dispatchEdgeSync(edge);
	}

	@Override
	public void dispatchCustomMsg(Message msg) {
		connectionHandler.sendToClient(msg);
		nextInChain.dispatchCustomMsg(msg);
	}

	@Override
	public void dispatchKeyClick(int keyCode) {
		connectionHandler.sendToClient(new KeyClick(keyCode));
		nextInChain.dispatchKeyClick(keyCode);
	}

	@Override
	public void chain(MessageDispatcher nextInChain) {
		this.nextInChain = nextInChain;
	}
}