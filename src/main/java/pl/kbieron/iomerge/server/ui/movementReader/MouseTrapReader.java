package pl.kbieron.iomerge.server.ui.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
import pl.kbieron.iomerge.server.gesture.GestureRecorder;
import pl.kbieron.iomerge.server.ui.InvisibleJFrame;

import javax.annotation.PostConstruct;
import javax.swing.Timer;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static java.awt.event.MouseEvent.BUTTON3;


@Component
public class MouseTrapReader extends InvisibleJFrame implements MovementReader, MouseListener, MouseMotionListener {

	private Point center;

	private Point oldMouseLocation;

	private boolean reading;

	private Timer timer;

	private MovementListener movementListener;

	@Autowired
	private VirtualScreen virtualScreen;

	@Autowired
	private GestureRecorder gestureReader;

	public MouseTrapReader() {
		super("MouseTrapReader");
	}

	@PostConstruct
	public void init() {
		movementListener = virtualScreen;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration().getBounds();
		setLocation(bounds.x, bounds.y);
		setSize(bounds.height, bounds.width);

		timer = new Timer(10, a -> readMove());

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				switch (keyEvent.getKeyCode()) {
					case KeyEvent.VK_ESCAPE:
						virtualScreen.exit();
				}
			}
		});
	}

	private void readMove() {
	}

	@Override
	public void startReading() {
		if ( reading ) return;
		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);
		center = getLocation();
		center.translate(getHeight() / 2, getWidth() / 2);

		timer.start();
	}

	@Override
	public void stopReading() {
		timer.stop();
		restoreMouseLocation();
		setVisible(false);
		reading = false;
	}

	private void restoreMouseLocation() {
		try {
			new Robot().mouseMove(oldMouseLocation.x, oldMouseLocation.y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		virtualScreen.mouseClicked();
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		virtualScreen.mousePressed();
		if ( mouseEvent.getButton() == BUTTON3 ) {
			movementListener = gestureReader;
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
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
	public void mouseMoved(MouseEvent mouseEvent) {
		if ( !reading ) return;
		Point mousePosition = mouseEvent.getLocationOnScreen();
		int dx = mousePosition.x - center.x;
		int dy = mousePosition.y - center.y;
		if ( dx != 0 || dy != 0 ) {
			movementListener.moveMouse(dx, dy);
			centerPointer();
		}
	}

	private void centerPointer() {
		try {
			new Robot().mouseMove(center.x, center.y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
