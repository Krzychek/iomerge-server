package com.github.krzychek.server.movementReader;


import com.github.krzychek.server.api.appState.AppState;
import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.api.movementReader.IOListener;
import com.github.krzychek.server.ui.UIHelper;
import com.github.krzychek.server.utils.BooleanCondition;
import com.github.krzychek.server.utils.IOListenerToAWTAdapter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.JFrame;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Arrays;


/**
 * MovementReader based on transparent JFrame, catches mose inside
 */
@Component
class MouseTrapReader extends JFrame {


	private final IOListener listener;
	private final AppStateManager appStateManager;
	private final Robot robot;
	private final MoveReadingThread readingThread;

	private volatile Point center;
	private BooleanCondition isReading = new BooleanCondition();

	@Autowired
	MouseTrapReader(IOListener listener, AppStateManager appStateManager) throws AWTException {
		super("IOMerge MovementReader");

		this.listener = listener;
		this.appStateManager = appStateManager;

		readingThread = new MoveReadingThread();
		readingThread.start();

		robot = new Robot();
	}

	@PostConstruct
	private void init() {
		// UI stuff
		reposition();
		UIHelper.makeInvisible(this, true);
		setAutoRequestFocus(true);

		IOListenerToAWTAdapter listenerAdapter = new IOListenerToAWTAdapter(listener);
		// add listener
		addMouseWheelListener(listenerAdapter);
		addMouseListener(listenerAdapter);
		addKeyListener(listenerAdapter);
	}

	private void reposition() {
		Rectangle displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
				.map(screen -> screen.getDefaultConfiguration().getBounds())
				.max((a, b) -> a.width * a.height - b.width * b.height)
				.orElseThrow(() -> new IllegalStateException("Problem with getting display dimension"));
		setLocation(displayRect.x, displayRect.y);
		setSize(displayRect.width, displayRect.height);
	}

	private void readMove() {
		if (isReading.no()) {
			Logger.warn("readMove called, but I'm not reading");
			return;
		}
		// absolute location
		Point move = MouseInfo.getPointerInfo().getLocation();
		// relative location
		move.translate(-center.x, -center.y);

		if (move.x != 0 || move.y != 0) {
			listener.move(move.x, move.y);
			centerMousePointer();
		}
	}

	private void centerMousePointer() {
		robot.mouseMove(center.x, center.y);
	}

	synchronized private void startReading() {
		if (isReading.yes()) return;

		setVisible(true);

		center = getLocation();
		center.translate(getWidth() / 2, getHeight() / 2);

		centerMousePointer();

		isReading.makeYes();
	}

	synchronized private void stopReading() {
		if (isReading.no()) return;
		isReading.makeNo();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}

		appStateManager.restoreMouse();
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

	@PreDestroy
	private void shutdown() {
		stopReading();
		readingThread.interrupt();
	}


	private class MoveReadingThread extends Thread {

		private volatile boolean interrupted = false;

		MoveReadingThread() {
			super("MouseTrapReader : mouse move reading thread");
		}

		@Override
		public void run() {

			while (!interrupted) {
				// await
				isReading.await();

				// read while is reading
				isReading.whileTrue(MouseTrapReader.this::readMove);
			}
		}

		public synchronized void interrupt() {
			interrupted = true;
			super.interrupt();
		}
	}

}
