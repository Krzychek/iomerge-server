package com.github.krzychek.iomerge.server.utils;

import com.github.krzychek.iomerge.server.api.movementReader.IOListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class IOListenerToAWTAdapter implements MouseWheelListener, MouseListener, KeyListener {

	private final IOListener listener;

	public IOListenerToAWTAdapter(IOListener listener) {
		this.listener = listener;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) { listener.mouseWheelMoved(e); }

	@Override
	public void mouseClicked(MouseEvent e) { listener.mouseClicked(e); }

	@Override
	public void mousePressed(MouseEvent e) { listener.mousePressed(e); }

	@Override
	public void mouseReleased(MouseEvent e) { listener.mouseReleased(e); }

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) { listener.keyTyped(e); }

	@Override
	public void keyPressed(KeyEvent e) { listener.keyPressed(e); }

	@Override
	public void keyReleased(KeyEvent e) { listener.keyReleased(e); }
}
