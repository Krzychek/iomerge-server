package pl.kbieron.iomerge.plugins.server.gesture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.movementReader.IOListenerAdapter;

import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.BUTTON3;


@Component
class GestureIOListener extends IOListenerAdapter {

	private final GestureRecorder gestureRecorder;

	private boolean capturing = false;

	@Autowired
	public GestureIOListener(GestureRecorder gestureRecorder) {
		this.gestureRecorder = gestureRecorder;
	}

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
	public void move(int dx, int dy) {
		if (!capturing) nextInChain.move(dx, dy);

		gestureRecorder.move(dx, dy);
	}
}