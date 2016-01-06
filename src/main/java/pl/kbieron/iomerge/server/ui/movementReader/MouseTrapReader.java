package pl.kbieron.iomerge.server.ui.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.gesture.GestureRecorder;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

import static java.awt.event.MouseEvent.BUTTON3;


class MouseTrapReader extends JFrame //
		implements MouseListener, MouseMotionListener, MouseWheelListener, AppStateListener {

	private Point center;

	private Point oldMouseLocation;

	private boolean reading;

	private Timer timer;

	private MovementListener movementListener;

	@Autowired
	private VirtualScreen virtualScreen;

	@Autowired
	private GestureRecorder gestureRecorder;

	private Robot robot;

	@PostConstruct
	private void init() throws AWTException {
		// set fields
		robot = new Robot();
		timer = new Timer(0, this::readMove);
		movementListener = virtualScreen;
		// UI stuff
		UIHelper.makeInvisible(this);
		setAutoRequestFocus(true);
		reposition();
		addListeners();
	}

	private void addListeners() {
		addKeyListener(virtualScreen);
		addMouseListener(this);
		addMouseWheelListener(this);
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
		}
		centerPointer();
	}

	private void centerPointer() {
		robot.mouseMove(center.x, center.y);
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		mouseEvent.getButton();
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if ( mouseEvent.getButton() == BUTTON3 ) {
			movementListener = gestureRecorder;
			gestureRecorder.mousePressed();
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
	public void mouseEntered(MouseEvent mouseEvent) {}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		movementListener.mouseWheelMoved(mouseWheelEvent.getWheelRotation());

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
