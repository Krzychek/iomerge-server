package pl.kbieron.iomerge.server.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JWindow;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public abstract class InvisibleJWindow extends JWindow {

	private final JComponent rootComponent;

	public InvisibleJWindow() {

		rootComponent = new JPanel();
		rootComponent.setOpaque(false);
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
		rootComponent.setCursor(blankCursor);
		setContentPane(rootComponent);
		setAlwaysOnTop(true);
		setBackground(new Color(0, 0, 0, 0));
	}
}
