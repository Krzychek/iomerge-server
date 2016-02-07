package pl.kbieron.iomerge.server.movementReader;

import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.ui.UIHelper;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;


class MouseTrapReader extends JFrame implements AppStateListener {

	private final MovementListener movementListener;

	private final Robot robot = new Robot();

	private Point center;

	private Point oldMouseLocation;

	private boolean reading;

	private final Timer timer = new Timer(0, this::readMove);

	@Inject
	MouseTrapReader(MovementListener movementListener, MouseWheelListener mouseWheelListener,
	                       MouseListener mouseListener, KeyListener keyListener) throws AWTException {

		super("IOMerge MovementReader");
		this.movementListener = movementListener;
		// UI stuff
		reposition();
		UIHelper.makeInvisible(this, true);
		setAutoRequestFocus(true);
		// add listeners
		addMouseWheelListener(mouseWheelListener);
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
	}

	private void reposition() {
		Rectangle displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
				.map(screen -> screen.getDefaultConfiguration().getBounds()) //
				.max((a, b) -> a.width * a.height) //
				.get();
		setLocation(displayRect.x, displayRect.y);
		setSize(displayRect.width, displayRect.height);
	}

	@SuppressWarnings( "UnusedParameters" )
	private void readMove(Object ignored) {
		if ( !reading )
			return;
		Point move = MouseInfo.getPointerInfo().getLocation();
		move.translate(-center.x, -center.y);

		if ( move.x != 0 || move.y != 0 ) {
			movementListener.move(move.x, move.y);
			centerPointer();
		}
	}

	private void centerPointer() {
		robot.mouseMove(center.x, center.y);
	}

	synchronized private void startReading() {
		if ( reading )
			return;
		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);

		center = getLocation();
		center.translate(getWidth() / 2, getHeight() / 2);

		centerPointer();

		timer.start();
	}

	synchronized private void stopReading() {
		if ( !reading )
			return;
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
		if ( AppState.ON_REMOTE == newState )
			startReading();
		else
			stopReading();
	}
}
