package pl.kbieron.iomerge.server.movementReader;


import org.pmw.tinylog.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.appState.AppState;
import pl.kbieron.iomerge.server.api.movementReader.IOListener;
import pl.kbieron.iomerge.server.ui.UIHelper;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;


/**
 * MovementReader based on transparent JFrame, catches mose inside
 */
@Component
class MouseTrapReader extends JFrame {


	private final IOListener listener;
	private final Robot robot;
	private final Timer timer;

	private volatile Point center;
	private Point oldMouseLocation;
	private volatile boolean reading;

	MouseTrapReader(IOListener listener) throws AWTException {
		super("IOMerge MovementReader");

		this.listener = listener;

		timer = new Timer(0, (ignored) -> readMove());
		robot = new Robot();
	}

	@PostConstruct
	private void init() {
		// UI stuff
		reposition();
		UIHelper.makeInvisible(this, true);
		setAutoRequestFocus(true);

		IOListenerAdapter listenerAdapter = new IOListenerAdapter(listener);
		// add listener
		addMouseWheelListener(listenerAdapter);
		addMouseListener(listenerAdapter);
		addKeyListener(listenerAdapter);
	}

	private void reposition() {
		Rectangle displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
				.map(screen -> screen.getDefaultConfiguration().getBounds()) //
				.max((a, b) -> a.width * b.height) //
				.orElseThrow(() -> new IllegalStateException("Problem with getting display dimension"));
		setLocation(displayRect.x, displayRect.y);
		setSize(displayRect.width, displayRect.height);
	}

	private void readMove() {
		if (!reading) {
			Logger.warn("readMove called, but I'm not reading");
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
		if (reading) return;
		reading = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);

		center = getLocation();
		center.translate(getWidth() / 2, getHeight() / 2);

		centerPointer();

		timer.start();
	}

	synchronized private void stopReading() {
		if (!reading) return;
		reading = false;
		timer.stop();
		restoreMouseLocation();
		setVisible(false);
	}

	@EventListener
	public void onStateChange(AppState newState) {
		if (AppState.ON_REMOTE == newState) {
			startReading();
		} else {
			stopReading();
		}
	}

	private void restoreMouseLocation() {
		robot.mouseMove(oldMouseLocation.x, oldMouseLocation.y);
	}

	private static class IOListenerAdapter implements MouseWheelListener, MouseListener, KeyListener {

		private final IOListener listener;

		IOListenerAdapter(IOListener listener) {
			this.listener = listener;
		}


		@Override
		public void mouseWheelMoved(MouseWheelEvent e) { listener.mouseWheelMoved(e); }

		@Override
		public void mouseClicked(MouseEvent e) { listener.mouseClicked(e); }

		@Override
		public void mousePressed(MouseEvent e) { listener.mousePressed(e); }

		@Override
		public void mouseReleased(MouseEvent e) { listener.mouseReleased(e); }

		@Override
		public void mouseEntered(MouseEvent e) { listener.mouseEntered(e); }

		@Override
		public void mouseExited(MouseEvent e) { listener.mouseExited(e); }

		@Override
		public void keyTyped(KeyEvent e) { listener.keyTyped(e); }

		@Override
		public void keyPressed(KeyEvent e) { listener.keyPressed(e); }

		@Override
		public void keyReleased(KeyEvent e) { listener.keyReleased(e); }
	}
}
