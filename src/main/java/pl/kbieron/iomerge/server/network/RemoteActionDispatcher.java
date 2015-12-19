package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.RemoteActionType;

import java.nio.ByteBuffer;


@Component
public class RemoteActionDispatcher {

	@Autowired
	EventServer eventServer;

	public void dispatchMouseSync(short x, short y) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteActionType.MOUSE_SYNC) //
				.putShort(x) //
				.putShort(y) //
				.array());
	}

	public void dispatchMousePress() {
		eventServer.sendToClient(RemoteActionType.MOUSE_PRESS);
	}

	public void dispatchMouseRelease() {
		eventServer.sendToClient(RemoteActionType.MOUSE_RELEASE);
	}

	public void dispatchHomeClick() {
		eventServer.sendToClient(RemoteActionType.HOME_BTN_CLICK);
	}

	public void dispatchBackClick() {
		eventServer.sendToClient(RemoteActionType.BACK_BTN_CLICK);
	}

	public void dispatchMenuClick() {
		eventServer.sendToClient(RemoteActionType.MENU_BTN_CLICK);
	}

	public void dispatchKeyPress(int keyCode) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteActionType.KEY_PRESS) //
				.putInt(keyCode) //
				.array());
	}

	public void dispatchKeyRelease(int keyCode) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteActionType.KEY_RELEASE) //
				.putInt(keyCode) //
				.array());
	}

	public void dispatchMouseWheelEvent(int wheelRotation) {
		eventServer.sendToClient(ByteBuffer.allocate(5) //
				.put(RemoteActionType.MOUSE_WHEEL) //
				.putInt(wheelRotation) //
				.array());
	}
}
