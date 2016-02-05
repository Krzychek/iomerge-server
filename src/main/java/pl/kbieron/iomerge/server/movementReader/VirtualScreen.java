package pl.kbieron.iomerge.server.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.network.MsgDispatcher;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;


public class VirtualScreen implements MovementListener, KeyListener {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private MsgDispatcher actionDispatcher;

	@ConfigProperty( "MovementScale" )
	private double movementScale = 1.5;

	private double unusedXMove = 0.0;

	private double unusedYMove = 0.0;

	private final static int[] modKeys = new int[]{ //
			KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT
	};

	static {
		Arrays.sort(modKeys);
	}

	@Override
	public void move(int dx, int dy) {
		unusedYMove += dy * movementScale;
		unusedXMove += dx * movementScale;
		actionDispatcher.dispatchMouseSync((int) unusedXMove, (int) unusedYMove);
		unusedXMove %= 1.0;
		unusedYMove %= 1.0;
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

		if ( keyCode == KeyEvent.VK_F4 )
			appStateManager.exitRemote();
		else if ( Arrays.binarySearch(modKeys, keyCode) != -1 )
			actionDispatcher.dispatchKeyPress(keyCode);
		else
			actionDispatcher.dispatchKeyClick(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();

		if ( Arrays.binarySearch(modKeys, keyCode) != -1 )
			actionDispatcher.dispatchKeyPress(keyCode);
	}

	public double getMovementScale() {
		return movementScale;
	}

	public void setMovementScale(double movementScale) {
		this.movementScale = movementScale;
	}
}
