package pl.kbieron.iomerge.server.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class UIHelper {

	/**
	 * @param frame - {@link JFrame} to make invisible
	 * @param hideCursor - if true cursor would be changed to transparent
	 */
	public static void makeInvisible(JFrame frame, boolean hideCursor) {

		frame.setUndecorated(true);
		makeWindowInvisible(frame);
		setInvisibleContentPane(frame, hideCursor);
	}

	/**
	 * @param frame - {@link JFrame} to make invisible, also hides cursor
	 */
	public static void makeInvisible(JFrame frame) {
		makeInvisible(frame, false);
	}

	private static void makeWindowInvisible(Window window) {

		window.setAlwaysOnTop(true);
		window.setBackground(new Color(0, 0, 0, 0));
	}

	private static void setInvisibleContentPane(RootPaneContainer container, boolean hideCursor) {

		JComponent rootComponent = new JPanel();
		rootComponent.setOpaque(false);

		if ( hideCursor ) {
			Cursor blankCursor = Toolkit.getDefaultToolkit()
					.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
			rootComponent.setCursor(blankCursor);
		}
		container.setContentPane(rootComponent);

	}
}
