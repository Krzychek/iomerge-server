package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.RemoteMsgTypes;

import java.nio.ByteBuffer;


@Component
public class RemoteMsgDispatcher {

	@Autowired
	EventServer eventServer;

	public void dispatchMouseSync(short x, short y) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteMsgTypes.MOUSE_SYNC) //
				.putShort(x) //
				.putShort(y) //
				.array());
	}

	public void dispatchMousePress() {
		eventServer.sendToClient(RemoteMsgTypes.MOUSE_PRESS);
	}

	public void dispatchMouseRelease() {
		eventServer.sendToClient(RemoteMsgTypes.MOUSE_RELEASE);
	}

	public void dispatchKeyPress(int keyCode) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteMsgTypes.KEY_PRESS) //
				.putInt(keyCode) //
				.array());
	}

	public void dispatchKeyRelease(int keyCode) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteMsgTypes.KEY_RELEASE) //
				.putInt(keyCode) //
				.array());
	}

	public void dispatchMouseWheelEvent(int wheelRotation) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteMsgTypes.MOUSE_WHEEL) //
				.putInt(wheelRotation) //
				.array());
	}

	public void dispatchCustomMsg(byte... msg) {
		eventServer.sendToClient(msg);
	}
}
