package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.RemoteActionFactory;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.utilities.Edge;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import java.awt.Point;


@Component
public class VirtualScreen implements MovementListener {

	private int width = 1000;

	private int height = 1000;

	private Edge edge;

	private boolean active;

	private Point actPosition = new Point();

	@Autowired
	private Director director;

	@Autowired
	private EventServer server;

	@Override
	public void move(int dx, int dy) {
		actPosition.x += dx;
		actPosition.y += dy;

		if ( actPosition.y < 0 ) actPosition.y = 0;
		else if ( actPosition.y < height ) actPosition.y = height;

		if ( edge == Edge.LEFT ) {
			if ( actPosition.x > width ) actPosition.x = width;
			else if ( actPosition.x < 0 ) exit();
		} else {
			if ( actPosition.x < 0 ) exit();
			else if ( actPosition.x > width ) actPosition.x = width;

		}
		byte[] mouseSync = RemoteActionFactory.createMouseSync((short) actPosition.x, (short) actPosition.y);
		server.sendToClient(mouseSync);

	}

	public void exit() {
		active = false;
		director.exitRemote();
	}

	public void enter(double y, Edge edge) {
		this.active = true;

		this.edge = edge;
		actPosition.x = (edge == Edge.LEFT ? width : 0);
		actPosition.y = (int) y * height;
	}

	@Override
	public void mouseClicked() {
		// TODO: implement
	}

	@Override
	public void MousePressed() {
		// TODO: implement
	}

	@Override
	public void mouseReleased() {
		// TODO: implement
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public void setX(int x) {
		actPosition.x = x;
	}

	public void setY(int y) {
		actPosition.y = y;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isActive() {
		return active;
	}

	public int getY() {
		return actPosition.y;
	}

	public int getX() {
		return actPosition.x;
	}

	public int getHeight() {
		return height;
	}
}
