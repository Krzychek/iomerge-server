package pl.kbieron.iomerge.server.ui.movementReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.deviceAbstraction.VirtualScreen;
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


@Component
public class MouseTrapReader extends InvisibleJFrame implements MovementReader {

	private Point center;

	private Point oldMouseLocation;

	private boolean catching;

	private Timer timer;

	@Autowired
	private VirtualScreen virtualScreen;

	private Director director;

	public MouseTrapReader() {
		super("MouseTrapReader");
	}

	@PostConstruct
	public void init() {
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
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();

		virtualScreen.moveMouse(mousePosition.x - center.x, mousePosition.y - center.y);

		centerPointer();
	}

	private void centerPointer() {
		try {
			new Robot().mouseMove(center.x, center.y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startReading() {
		if ( catching ) return;
		catching = true;

		oldMouseLocation = MouseInfo.getPointerInfo().getLocation();

		setVisible(true);
		center = getLocation();
		center.translate(getHeight() / 2, getWidth() / 2);

		timer.start();
	}

	@Override
	public void stopReading() {
		timer.stop();
		setVisible(false);
		restoreMouseLocation();
		catching = false;
	}

	private void restoreMouseLocation() {
		try {
			new Robot().mouseMove(oldMouseLocation.x, oldMouseLocation.y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
