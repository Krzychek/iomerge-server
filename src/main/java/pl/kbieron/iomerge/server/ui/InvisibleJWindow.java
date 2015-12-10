package pl.kbieron.iomerge.server.ui;

import javax.swing.JPanel;
import javax.swing.JWindow;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public abstract class InvisibleJWindow extends JWindow {

	public InvisibleJWindow(String s) {

		JPanel jPanel = new JPanel();
		jPanel.setOpaque(false);
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
		jPanel.setCursor(blankCursor);

		setContentPane(jPanel);

		//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//		setUndecorated(true);
		setAlwaysOnTop(true);
		//		setResizable(false);
		setBackground(new Color(0, 0, 0, 0));
	}
}
