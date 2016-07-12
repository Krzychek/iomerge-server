package pl.kbieron.iomerge.server.api.movementReader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Receives mouse events
 */
public interface IOListener extends MouseListener, MouseWheelListener, KeyListener {

	default void move(int dx, int dy) {}

	@Override
	default void keyTyped(KeyEvent e) {}

	@Override
	default void keyPressed(KeyEvent e) {}

	@Override
	default void keyReleased(KeyEvent e) {}

	@Override
	default void mouseClicked(MouseEvent e) {}

	@Override
	default void mousePressed(MouseEvent e) {}

	@Override
	default void mouseReleased(MouseEvent e) {}

	@Override
	default void mouseEntered(MouseEvent e) {}

	@Override
	default void mouseExited(MouseEvent e) {}

	@Override
	default void mouseWheelMoved(MouseWheelEvent e) {}

}
