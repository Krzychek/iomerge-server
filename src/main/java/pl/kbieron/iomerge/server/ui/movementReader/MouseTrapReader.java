package pl.kbieron.iomerge.server.ui.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.ui.UIHelper;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Arrays;


class MouseTrapReader extends JFrame implements AppStateListener {

	private final MovementListener movementListener;

	private Point center;

	private Point oldMouseLocation;

	private boolean reading;

	private Timer timer;

	private Robot robot;

	@Autowired
	public MouseTrapReader(CompositeListener compositeListener) {
		this.movementListener = compositeListener;
		addKeyListener(compositeListener);
		addMouseListener(compositeListener);
		addMouseWheelListener(compositeListener);
	}

	@PostConstruct
	private void init() throws AWTException {
		// set fields
		robot = new Robot();
		timer = new Timer(0, this::readMove);
		// UI stuff
		UIHelper.makeInvisible(this);
		setAutoRequestFocus(true);
		reposition();
	}

	private void reposition() {
		Rectangle displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
				.map(screen -> screen.getDefaultConfiguration().getBounds()) //
				.max((a, b) -> a.x * a.y - b.x * b.y) //
				.get();
		setLocation(displayRect.x, displayRect.y);
		setSize(displayRect.width, displayRect.height);
	}

	@SuppressWarnings( "UnusedParameters" )
	synchronized private void readMove(Object ignored) {
		if ( !reading ) return;
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
		if ( reading ) return;
		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);

		Point location = getLocation();
		robot.mouseMove(location.x + getWidth() - 1, location.y + getHeight() - 1);

		location.translate(getWidth() / 2, getHeight() / 2);
		center = location;

		centerPointer();

		timer.start();
	}

	synchronized private void stopReading() {
		if ( !reading ) return;
		reading = false;
		timer.stop();
		restoreMouseLocation();
		setVisible(false);
	}

	private void restoreMouseLocation() {
		robot.mouseMove(oldMouseLocation.x, oldMouseLocation.y);
	}

	@Override
	public void onApplicationEvent(AppState.UpdateEvent appStateUpdateEvent) {
		if ( AppState.ON_REMOTE == appStateUpdateEvent.getStateChange() ) startReading();
		else stopReading();
	}
}
