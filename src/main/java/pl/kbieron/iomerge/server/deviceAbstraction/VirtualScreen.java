package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.DeviceState;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.utilities.Edge;


@Component
public class VirtualScreen {

	private int width;

	private int height;

	private Edge edge;

	private boolean active;

	private DeviceState state = new DeviceState();

	@Autowired
	private Director director;

	public void moveMouse(int dx, int dy) {
		state.x += dx;
		state.y += dy;

		if ( state.y < 0 ) state.y = 0;
		else if ( state.y < height ) state.y = height;

		if ( edge == Edge.LEFT ) {
			if ( state.x > width ) state.x = width;
			else if ( state.x < 0 ) exit();
		} else {
			if ( state.x > width ) exit();
			else if ( state.x < 0 ) state.x = 0;
		}
	}

	public void exit() {
		active = false;
		director.exitRemote(state.y / height);
	}

	public void enter(double y) {
		this.active = true;

		state.x = edge == Edge.LEFT ? width : 0;
		state.y = (int) y * height;
	}

	public void mouseClicked() {
		// TODO: implement
	}

	public void mousePressed() {
		// TODO: implement
	}

	public void mouseReleased() {
		// TODO: implement
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
		state.x = x;
		return this;
	}

	public VirtualScreen setY(int y) {
		state.y = y;
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
		return state.y;
	}

	public int getX() {
		return state.x;
	}

	public int getHeight() {
		return height;
	}
}
