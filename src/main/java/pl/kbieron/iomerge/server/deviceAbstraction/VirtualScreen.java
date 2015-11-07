package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.utilities.Edge;


@Component
public class VirtualScreen {

	private int x;

	private int y;

	private int width;

	private int height;

	private Edge edge;

	private boolean active;

	@Autowired
	private Director director;

	public void moveMouse(int dx, int dy) {
		x += dx;
		y += dy;

		if ( y < 0 ) y = 0;
		else if ( y < height ) y = height;

		if ( edge == Edge.LEFT ) {
			if ( x > width ) x = width;
			else if ( x < 0 ) exit();
		} else {
			if ( x > width ) exit();
			else if ( x < 0 ) x = 0;
		}
	}

	public void exit() {
		active = false;
		director.exitRemote(y / height);
	}

	public void enter(double y) {
		this.active = true;

		x = edge == Edge.LEFT ? 0 : height;
		this.y = (int) y * height;
	}

	public VirtualScreen setWidth(int width) {
		this.width = width;
		return this;
	}

	public VirtualScreen setEdge(Edge edge) {
		this.edge = edge;
		return this;
	}

	public VirtualScreen setX(int x) {
		this.x = x;
		return this;
	}

	public VirtualScreen setY(int y) {
		this.y = y;
		return this;
	}

	public VirtualScreen setHeight(int height) {
		this.height = height;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getHeight() {
		return height;
	}
}
