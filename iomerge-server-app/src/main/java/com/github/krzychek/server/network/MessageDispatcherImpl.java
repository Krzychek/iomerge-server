package com.github.krzychek.server.network;

import com.github.krzychek.server.api.network.MessageDispatcher;
import com.github.krzychek.server.model.Edge;
import com.github.krzychek.server.model.message.Message;
import com.github.krzychek.server.model.message.keyboard.KeyClick;
import com.github.krzychek.server.model.message.keyboard.KeyPress;
import com.github.krzychek.server.model.message.keyboard.KeyRelease;
import com.github.krzychek.server.model.message.misc.ClipboardSync;
import com.github.krzychek.server.model.message.misc.EdgeSync;
import com.github.krzychek.server.model.message.mouse.MouseMove;
import com.github.krzychek.server.model.message.mouse.MousePress;
import com.github.krzychek.server.model.message.mouse.MouseRelease;
import com.github.krzychek.server.model.message.mouse.MouseWheel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Order(0)
@Component
public class MessageDispatcherImpl implements MessageDispatcher {

	private final ConnectionHandler connectionHandler;

	private MessageDispatcher nextInChain;

	@Autowired
	public MessageDispatcherImpl(ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void dispatchMouseMove(int x, int y) {
		connectionHandler.sendToClient(new MouseMove(x, y));
		nextInChain.dispatchMouseMove(x, y);
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
