package pl.kbieron.iomerge.server.ui.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.server.gesture.GestureRecorder;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import static java.awt.event.MouseEvent.BUTTON3;


class CompositeListener implements MovementListener, MouseListener, MouseWheelListener, KeyListener {

	private MovementListener movementListener;

	@Autowired
	private VirtualScreen virtualScreen;

	@Autowired
	private GestureRecorder gestureRecorder;

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if ( mouseEvent.getButton() == BUTTON3 ) {
			movementListener = gestureRecorder;
		}
		movementListener.mousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		movementListener.mouseReleased();
		if ( mouseEvent.getButton() == BUTTON3 ) {
			movementListener = virtualScreen;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		movementListener.mouseWheelMoved(mouseWheelEvent.getWheelRotation());
	}

	@Override
	public void move(int dx, int dy) {
		movementListener.move(dx, dy);
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {virtualScreen.keyTyped(keyEvent);}

	@Override
	public void keyPressed(KeyEvent keyEvent) {virtualScreen.keyPressed(keyEvent);}

	@Override
	public void keyReleased(KeyEvent keyEvent) {virtualScreen.keyReleased(keyEvent);}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {}
}