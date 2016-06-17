package pl.kbieron.iomerge.server.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.GestureRecorder;

import java.awt.event.*;

import static java.awt.event.MouseEvent.BUTTON3;


@Component
public class CompositeListener implements MovementListener, MouseListener, MouseWheelListener, KeyListener {

	private MovementListener movementListener;

	private VirtualScreen virtualScreen;

	private GestureRecorder gestureRecorder;

	@Autowired
	public CompositeListener(VirtualScreen virtualScreen, GestureRecorder gestureRecorder) {
		this.gestureRecorder = gestureRecorder;
		this.virtualScreen = virtualScreen;
		this.movementListener = virtualScreen;
	}

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