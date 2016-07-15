package pl.kbieron.iomerge.server.movementReader;

import org.annoprops.annotations.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;
import pl.kbieron.iomerge.server.api.movementReader.IOListener;
import pl.kbieron.iomerge.server.api.network.MessageDispatcher;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;


/**
 * Models device on server side, proxy between dispatcher msg dispatcher and whole module
 */
@Order(0)
@Component
public class VirtualScreen implements IOListener {

	private final static int[] modKeys = new int[]{ //
			KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT
	};

	static {
		Arrays.sort(modKeys);
	}

	private final AppStateManager appStateManager;

	private final MessageDispatcher actionDispatcher;

	@ConfigProperty("MovementScale")
	private double movementScale = 1.5;

	private double unusedXMove = 0.0;

	private double unusedYMove = 0.0;

	private IOListener nextInChain;

	@Autowired
	public VirtualScreen(MessageDispatcher actionDispatcher, AppStateManager appStateManager) {
		this.actionDispatcher = actionDispatcher;
		this.appStateManager = appStateManager;
	}

	@Override
	public void move(int dx, int dy) {
		unusedYMove += dy * movementScale;
		unusedXMove += dx * movementScale;
		actionDispatcher.dispatchMouseSync((int) unusedXMove, (int) unusedYMove);
		unusedXMove %= 1.0;
		unusedYMove %= 1.0;

		nextInChain.move(dx, dy);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		nextInChain.keyTyped(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		actionDispatcher.dispatchMousePress();
		nextInChain.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		actionDispatcher.dispatchMouseRelease();
		nextInChain.mouseReleased(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		actionDispatcher.dispatchMouseWheelEvent(e.getWheelRotation());
		nextInChain.mouseWheelMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_F4)
			appStateManager.exitRemote();
		else if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyPress(keyCode);
		else
			actionDispatcher.dispatchKeyClick(keyCode);

		nextInChain.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyRelease(keyCode);

		nextInChain.keyReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		nextInChain.mouseClicked(e);
	}

	public double getMovementScale() {
		return movementScale;
	}

	public void setMovementScale(double movementScale) {
		this.movementScale = movementScale;
	}

	@Override
	public void chain(IOListener nextInChain) {
		this.nextInChain = nextInChain;
	}
}
