package pl.kbieron.iomerge.plugins.server.gesture;

import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.movementReader.IOListener;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static java.awt.event.MouseEvent.BUTTON3;


@Component
class GestureIOListener implements IOListener {


	private final GestureRecorder gestureRecorder;

	private IOListener nextInChain;
	private boolean capturing = false;

	public GestureIOListener(GestureRecorder gestureRecorder) {this.gestureRecorder = gestureRecorder;}

	@Override
	public void mousePressed(MouseEvent e) {
		if (BUTTON3 != e.getButton())
			nextInChain.mousePressed(e);

		capturing = true;
		gestureRecorder.startGesture();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (BUTTON3 != e.getButton())
			nextInChain.mouseReleased(e);

		capturing = false;
		gestureRecorder.finishGesture();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		nextInChain.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		nextInChain.mouseExited(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		nextInChain.mouseWheelMoved(e);
	}

	@Override
	public void move(int dx, int dy) {
		if (!capturing) nextInChain.move(dx, dy);

		gestureRecorder.move(dx, dy);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		nextInChain.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		nextInChain.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		nextInChain.keyReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		nextInChain.mouseClicked(e);
	}

	@Override
	public void chain(IOListener nextInChain) {
		this.nextInChain = nextInChain;
	}
}