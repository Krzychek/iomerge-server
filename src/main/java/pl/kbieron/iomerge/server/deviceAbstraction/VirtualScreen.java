package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.network.RemoteMsgDispatcher;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class VirtualScreen implements MovementListener, KeyListener {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private RemoteMsgDispatcher actionDispatcher;

	@ConfigProperty( "MovementScale" )
	private double movementScale = 1.5;

	private double unusedXMove = 0.0;

	private double unusedYMove = 0.0;

	@Override
	public void move(int dx, int dy) {
		unusedYMove += dy * movementScale;
		unusedXMove += dx * movementScale;
		actionDispatcher.dispatchMouseSync((short) unusedXMove, (short) unusedYMove);
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
		if ( keyCode == KeyEvent.VK_F12 ) appStateManager.exitRemote();
		else actionDispatcher.dispatchKeyPress(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		actionDispatcher.dispatchKeyRelease(keyEvent.getKeyCode());
	}

	public double getMovementScale() {
		return movementScale;
	}

	public void setMovementScale(double movementScale) {
		this.movementScale = movementScale;
	}
}
