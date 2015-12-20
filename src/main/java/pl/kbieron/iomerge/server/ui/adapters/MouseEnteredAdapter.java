package pl.kbieron.iomerge.server.ui.adapters;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


@FunctionalInterface
public interface MouseEnteredAdapter extends MouseListener {

	@Override
	default void mouseClicked(MouseEvent mouseEvent) {}

	@Override
	default void mousePressed(MouseEvent mouseEvent) {}

	@Override
	default void mouseReleased(MouseEvent mouseEvent) {}

	@Override
	default void mouseExited(MouseEvent mouseEvent) {}

	@Override
	void mouseEntered(MouseEvent mouseEvent);
}
