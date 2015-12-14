package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.RemoteActionFactory;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.utilities.Edge;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


@Component
public class VirtualScreen implements MovementListener, KeyListener {

	private short width = 1000;

	private short height = 1000;

	private Edge edge;

	private boolean active;

	private short positionX;

	private short positionY;

	@Autowired
	private Director director;

	@Autowired
	private EventServer server;

	@Override
	public void move(int dx, int dy) {
		positionX += dx;
		positionY += dy;

		if ( positionY < 0 ) positionY = 0;
		else if ( positionY < height ) positionY = height;

		if ( edge == Edge.LEFT ) {
			if ( positionX > width ) positionX = width;
			else if ( positionX < 0 ) exit();
		} else {
			if ( positionX < 0 ) exit();
			else if ( positionX > width ) positionX = width;

		}
		// TODO replace with position
		byte[] mouseSync = RemoteActionFactory.createMouseSync((short) dx, (short) dy);
		server.sendToClient(mouseSync);

	}

	public void exit() {
		active = false;
		director.exitRemote();
	}

	public void enter(double y, Edge edge) {
		this.active = true;

		this.edge = edge;
		positionX = (edge == Edge.LEFT ? width : 0);
		positionY = (short) (y * height);
	}

	@Override
	public void mousePressed() {
		byte[] mousePress = RemoteActionFactory.createMousePress();
		server.sendToClient(mousePress);
	}

	@Override
	public void mouseReleased() {
		byte[] mousePress = RemoteActionFactory.createMouseRelease();
		server.sendToClient(mousePress);
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public boolean isActive() {
		return active;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		server.sendToClient(RemoteActionFactory.createKeyPress(keyEvent.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		server.sendToClient(RemoteActionFactory.createKeyRelease(keyEvent.getKeyCode()));
	}
}
