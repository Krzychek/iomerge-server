package pl.kbieron.iomerge.server.api.movementReader;

import pl.kbieron.iomerge.server.api.Chainable;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
 * Receives io application events
 *
 * @see Chainable
 */
public interface IOListener extends Chainable<IOListener> {

	void move(int dx, int dy);

	void keyTyped(KeyEvent e);

	void keyPressed(KeyEvent e);

	void keyReleased(KeyEvent e);

	void mouseClicked(MouseEvent e);

	void mousePressed(MouseEvent e);

	void mouseReleased(MouseEvent e);

	void mouseEntered(MouseEvent e);

	void mouseExited(MouseEvent e);

	void mouseWheelMoved(MouseWheelEvent e);

}
