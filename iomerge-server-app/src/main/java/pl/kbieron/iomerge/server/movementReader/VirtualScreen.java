package pl.kbieron.iomerge.server.movementReader;

import org.annoprops.annotations.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;
import pl.kbieron.iomerge.server.api.movementReader.IOListener;
import pl.kbieron.iomerge.server.api.network.MessageDispatcher;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;


/**
 * Models device on server side, proxy between dispatcher msg dispatcher and whole module
 */
@Component
public class VirtualScreen implements IOListener, KeyListener {

	private final static int[] modKeys = new int[]{ //
			KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT
	};

	static {
		Arrays.sort(modKeys);
	}

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private MessageDispatcher actionDispatcher;

	@ConfigProperty("MovementScale")
	private double movementScale = 1.5;

	private double unusedXMove = 0.0;

	private double unusedYMove = 0.0;

	@Override
	public void move(int dx, int dy) {
		unusedYMove += dy * movementScale;
		unusedXMove += dx * movementScale;
		actionDispatcher.dispatchMouseSync((int) unusedXMove, (int) unusedYMove);
		unusedXMove %= 1.0;
		unusedYMove %= 1.0;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		actionDispatcher.dispatchMousePress();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		actionDispatcher.dispatchMouseRelease();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		actionDispatcher.dispatchMouseWheelEvent(e.getWheelRotation());
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();

		if (keyCode == KeyEvent.VK_F4)
			appStateManager.exitRemote();
		else
			if (Arrays.binarySearch(modKeys, keyCode) >= 0)
				actionDispatcher.dispatchKeyPress(keyCode);
			else
				actionDispatcher.dispatchKeyClick(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();

		if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyRelease(keyCode);
	}

	public double getMovementScale() {
		return movementScale;
	}

	public void setMovementScale(double movementScale) {
		this.movementScale = movementScale;
	}
}
