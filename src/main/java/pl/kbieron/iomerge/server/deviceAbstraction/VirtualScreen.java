package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.network.RemoteMsgDispatcher;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class VirtualScreen implements MovementListener, KeyListener {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private RemoteMsgDispatcher actionDispatcher;

	@Override
	public void move(int dx, int dy) {
		actionDispatcher.dispatchMouseSync((short) dx, (short) dy);
	}

	@Override
	public void mousePressed() {
		actionDispatcher.dispatchMousePress();
	}

	@Override
	public void mouseReleased() {
		actionDispatcher.dispatchMouseRelease();
	}

	@Override
	public void mouseWheelMoved(int wheelRotation) {
		actionDispatcher.dispatchMouseWheelEvent(wheelRotation);
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();
		if ( keyCode == KeyEvent.VK_F12 ) appStateManager.exitRemote();
		else actionDispatcher.dispatchKeyPress(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		actionDispatcher.dispatchKeyRelease(keyEvent.getKeyCode());
	}
}
