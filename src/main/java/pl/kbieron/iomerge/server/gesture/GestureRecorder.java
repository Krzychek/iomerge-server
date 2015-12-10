package pl.kbieron.iomerge.server.gesture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.alghorithm.Recognizer;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.ui.movementReader.MovementListener;


@Component
public class GestureRecorder implements MovementListener {

	@Autowired
	private Recognizer recognizer;

	private int width;

	private int height;

	private boolean active;

	private Input.Builder inputBuilder = Input.builder();

	@Override
	public void moveMouse(int dx, int dy) {
		inputBuilder.move(dx, dy);
	}

	@Override
	public void mousePressed() {
	}

	@Override
	public void mouseReleased() {
		recognizer.recognize(inputBuilder.build());
		inputBuilder = Input.builder();
	}

}
