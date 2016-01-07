package pl.kbieron.iomerge.server.ui.movementReader;

import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;


public interface CompositeListener extends MovementListener, MouseListener, MouseWheelListener, KeyListener {

	@Override
	default void mouseClicked(MouseEvent mouseEvent) {}

	@Override
	default void mouseEntered(MouseEvent mouseEvent) {}

	@Override
	default void mouseExited(MouseEvent mouseEvent) {}
}
