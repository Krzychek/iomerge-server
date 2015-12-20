package pl.kbieron.iomerge.server.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.RootPaneContainer;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;


public class UIHelper {

	public static void makeInvisible(JWindow window) {

		makeWindowInvisible(window);
		setInvisibleContentPane(window);
	}

	public static void makeInvisible(JFrame frame) {

		frame.setUndecorated(true);
		makeWindowInvisible(frame);
		setInvisibleContentPane(frame);
	}

	private static void makeWindowInvisible(Window window) {

		window.setAlwaysOnTop(true);
		//		window.setBackground(new Color(0, 0, 0, 0));
	}

	private static void setInvisibleContentPane(RootPaneContainer container) {

		JComponent rootComponent = new JPanel();
		rootComponent.setOpaque(false);
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
		rootComponent.setCursor(blankCursor);
		container.setContentPane(rootComponent);

	}
}
