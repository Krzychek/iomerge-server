package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.appState.StateObserver;
import pl.kbieron.iomerge.server.appState.StateType;
import pl.kbieron.iomerge.server.network.RemoteActionDispatcher;
import pl.kbieron.iomerge.server.utilities.Edge;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class VirtualScreen implements MovementListener, KeyListener, StateObserver {

	private short width = 1000;

	private short height = 1000;

	private Edge edge;

	private short positionX;

	private short positionY;

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private RemoteActionDispatcher actionDispatcher;

	@Override
	public void move(int dx, int dy) {
		positionX += dx;
		positionY += dy;

		if ( positionY < 0 ) positionY = 0;
		else if ( positionY < height ) positionY = height;

		if ( edge == Edge.LEFT ) {
			if ( positionX > width ) positionX = width;
			else if ( positionX < 0 ) appStateManager.exitRemote();
		} else {
			if ( positionX < 0 ) appStateManager.exitRemote();
			else if ( positionX > width ) positionX = width;
		}

		// TODO replace with position
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

	public void setWidth(short width) {
		this.width = width;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		actionDispatcher.dispatchKeyPress(keyEvent.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		actionDispatcher.dispatchKeyRelease(keyEvent.getKeyCode());
	}

	@Override
	public void update(AppStateManager appStateManager) {
		if ( appStateManager.getStateType() == StateType.ON_REMOTE ) {
			enter(appStateManager.getEnterPosition());
		}
	}

	public void enter(double y) {
		positionX = (edge == Edge.LEFT ? width : 0);
		positionY = (short) (y * height);
	}
}
