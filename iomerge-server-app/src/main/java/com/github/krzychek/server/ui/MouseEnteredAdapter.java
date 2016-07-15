package com.github.krzychek.server.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


@FunctionalInterface
interface MouseEnteredAdapter extends MouseListener {

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
