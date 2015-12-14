package pl.kbieron.iomerge.server.ui.movementReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
import pl.kbieron.iomerge.server.gesture.GestureRecorder;
import pl.kbieron.iomerge.server.ui.InvisibleJWindow;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import javax.annotation.PostConstruct;
import javax.swing.Timer;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static java.awt.event.MouseEvent.BUTTON3;


@Component
public class MouseTrapReader extends InvisibleJWindow implements MovementReader, MouseListener, MouseMotionListener,
		KeyListener {

	private final Log log = LogFactory.getLog(MouseTrapReader.class);

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
	private void init() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			log.error("failed to create robot: ", e);
			throw new RuntimeException();
		}
		movementListener = virtualScreen;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration().getBounds();
		setLocation(bounds.x, bounds.y);
		setSize(bounds.width, bounds.height);
		timer = new Timer(0, this::readMove);

		addKeyListener(this);
		addMouseListener(this);
	}

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
		if ( reading ) {
			robot.mouseMove(center.x, center.y);
		}
	}

	@Override
	synchronized public void startReading() {
		if ( reading ) return;
		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);

		Point location = getLocation();
		robot.mouseMove(location.x + getWidth() - 1, location.y + getHeight() - 1);

		location.translate(getHeight() / 2, getWidth() / 2);
		center = location;

		centerPointer();

		timer.start();
	}

	@Override
	synchronized public void stopReading() {
		timer.stop();
		restoreMouseLocation();
		setVisible(false);
		reading = false;
	}

	private void restoreMouseLocation() {
		robot.mouseMove(oldMouseLocation.x, oldMouseLocation.y);
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {}

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
	public void keyPressed(KeyEvent keyEvent) {
		keyEvent.getKeyCode();
		//TODO
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		keyEvent.getKeyCode();
		//TODO
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
	public void keyTyped(KeyEvent keyEvent) {}

}
