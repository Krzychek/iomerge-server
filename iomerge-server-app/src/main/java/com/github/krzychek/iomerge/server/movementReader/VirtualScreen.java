package com.github.krzychek.iomerge.server.movementReader;

import com.github.krzychek.iomerge.server.api.appState.AppStateManager;
import com.github.krzychek.iomerge.server.api.movementReader.IOListener;
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher;
import org.annoprops.annotations.ConfigProperty;
import org.annoprops.annotations.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;


/**
 * Models device on server side, proxy between dispatcher msg dispatcher and whole module
 */
@Order(0)
@Component
@PropertyHolder
public class VirtualScreen implements IOListener {

	private final static int[] modKeys = new int[]{ //
			KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT
	};

	static {
		Arrays.sort(modKeys);
	}

	private final AppStateManager appStateManager;

	private final MessageDispatcher actionDispatcher;

	@ConfigProperty("MovementScale")
	private double movementScale = 1.5;
	@ConfigProperty("ReverseScroll")
	private boolean reverseScroll = false;
	private double unusedXMove = 0.0;
	private double unusedYMove = 0.0;
	private IOListener nextInChain;

	@Autowired
	public VirtualScreen(MessageDispatcher actionDispatcher, AppStateManager appStateManager) {
		this.actionDispatcher = actionDispatcher;
		this.appStateManager = appStateManager;
	}

	@Override
	public void move(int dx, int dy) {
		unusedYMove += dy * movementScale;
		unusedXMove += dx * movementScale;
		actionDispatcher.dispatchMouseMove((int) unusedXMove, (int) unusedYMove);
		unusedXMove %= 1.0;
		unusedYMove %= 1.0;

		nextInChain.move(dx, dy);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		nextInChain.keyTyped(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		actionDispatcher.dispatchMousePress();
		nextInChain.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		actionDispatcher.dispatchMouseRelease();
		nextInChain.mouseReleased(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int wheelRotation = reverseScroll ? -e.getWheelRotation() : e.getWheelRotation();
		actionDispatcher.dispatchMouseWheelEvent(wheelRotation);

		nextInChain.mouseWheelMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_F4)
			appStateManager.restoreMouse();
		else if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyPress(keyCode);
		else
			actionDispatcher.dispatchKeyClick(keyCode);

		nextInChain.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyRelease(keyCode);

		nextInChain.keyReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		nextInChain.mouseClicked(e);
	}

	public double getMovementScale() {
		return movementScale;
	}

	public void setMovementScale(double movementScale) {
		this.movementScale = movementScale;
	}

	public boolean isReverseScroll() {
		return reverseScroll;
	}

	public void setReverseScroll(boolean reverseScroll) {
		this.reverseScroll = reverseScroll;
	}

	@Override
	public void chain(IOListener nextInChain) {
		this.nextInChain = nextInChain;
	}
}
