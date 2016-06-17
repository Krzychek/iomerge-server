package pl.kbieron.iomerge.server.movementReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.ui.UIHelper;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


/**
 * MovementReader based on transparent JFrame, catches mose inside
 */
@Component
class MouseTrapReader extends JFrame implements AppStateListener {

	private static final Logger log = Logger.getLogger(MouseTrapReader.class);

	private final Robot robot = new Robot();
	@Autowired
	private CompositeListener listener;
	private volatile Point center;
	private Point oldMouseLocation;
	private volatile boolean reading;
	private final Timer timer = new Timer(0, (ignored) -> readMove());

	MouseTrapReader() throws AWTException {
		super("IOMerge MovementReader");
	}

	@PostConstruct
	private void init() {
		// UI stuff
		reposition();
		UIHelper.makeInvisible(this, true);
		setAutoRequestFocus(true);

		// add listener
		addMouseWheelListener(listener);
		addMouseListener(listener);
		addKeyListener(listener);
	}

	private void reposition() {
		Rectangle displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
				.map(screen -> screen.getDefaultConfiguration().getBounds()) //
				.max((a, b) -> a.width * a.height) //
				.orElseThrow(() -> new IllegalStateException("Problem with getting display dimension"));
		setLocation(displayRect.x, displayRect.y);
		setSize(displayRect.width, displayRect.height);
	}

	private void readMove() {
		if (!reading) {
			log.warn("readMove called, but I'm not reading");
			return;
		}
		Point move = MouseInfo.getPointerInfo().getLocation();
		move.translate(-center.x, -center.y);

		if (move.x != 0 || move.y != 0) {
			listener.move(move.x, move.y);
			centerPointer();
		}
	}

	private void centerPointer() {
		robot.mouseMove(center.x, center.y);
	}

	synchronized private void startReading() {
		if (reading) {
			log.warn("startReading called, but I'm already reading");
			return;
		}

		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);

		center = getLocation();
		center.translate(getWidth() / 2, getHeight() / 2);

		centerPointer();

		timer.start();
	}

	synchronized private void stopReading() {
		if (!reading) {
			log.warn("stopReading called, but I'm already reading");
			return;
		}
		reading = false;
		timer.stop();
		restoreMouseLocation();
		setVisible(false);
	}

	private void restoreMouseLocation() {
		robot.mouseMove(oldMouseLocation.x, oldMouseLocation.y);
	}

	@Override
	public void onStateChange(AppState newState) {
		if (AppState.ON_REMOTE == newState) {
			startReading();
		} else {
			stopReading();
		}
	}
}
