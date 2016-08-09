package com.github.krzychek.iomerge.server.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;


public class UIHelper {

	/**
	 * @param frame - {@link JFrame} to make invisible
	 */
	public static void makeJFrameInvisible(JFrame frame) {

		frame.setUndecorated(true);
		makeWindowInvisible(frame);
		setInvisibleContentPane(frame);
		hideCursor(frame.getRootPane());

	}

	/**
	 * @param window - {@link Window} to make invisible
	 */
	static void makeWindowInvisible(Window window) {
		window.setAlwaysOnTop(true);
		window.setBackground(new Color(0, 0, 0, 0));
		window.setType(Window.Type.POPUP);
	}

	private static void setInvisibleContentPane(RootPaneContainer container) {
		JComponent rootComponent = new JPanel();
		rootComponent.setOpaque(false);

		// hide cursor
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
		rootComponent.setCursor(blankCursor);

		container.setContentPane(rootComponent);
	}

	private static void hideCursor(JComponent rootComponent) {
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
		rootComponent.setCursor(blankCursor);
	}
}
