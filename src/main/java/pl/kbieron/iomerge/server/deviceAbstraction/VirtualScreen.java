package pl.kbieron.iomerge.server.deviceAbstraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.DeviceState;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.ui.movementReader.MovementListener;
import pl.kbieron.iomerge.server.utilities.Edge;


@Component
public class VirtualScreen implements MovementListener {

	private int width = 1000;

	private int height = 1000;

	private Edge edge;

	private boolean active;

	private DeviceState state = new DeviceState();

	@Autowired
	private Director director;

	@Override
	public void moveMouse(int dx, int dy) {
		state.x += dx;
		state.y += dy;

		if ( state.y < 0 ) state.y = 0;
		else if ( state.y < height ) state.y = height;

		if ( edge == Edge.LEFT ) {
			if ( state.x > width ) state.x = width;
			else if ( state.x < 0 ) exit();
		} else {
			if ( state.x < 0 ) exit();
			else if ( state.x < 0 ) state.x = 0;
		}
	}

	public void exit() {
		active = false;
		director.exitRemote(state.y / height);
	}

	public void enter(double y) {
		this.active = true;

		state.x = (edge == Edge.LEFT ? width : 0);
		state.y = (int) y * height;
	}

	@Override
	public void mouseClicked() {
		// TODO: implement
	}

	@Override
	public void mousePressed() {
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
		state.x = x;
	}

	public void setY(int y) {
		state.y = y;
	}

	public void setHeight(int height) {
		this.height = height;
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
