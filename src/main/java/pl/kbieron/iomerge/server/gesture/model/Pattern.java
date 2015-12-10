package pl.kbieron.iomerge.server.gesture.model;

import pl.kbieron.iomerge.model.ClientAction;

import java.awt.Point;
import java.util.List;


public class Pattern implements Template {

	List<Point> points;

	private ClientAction action;

	public Pattern(List<Point> points, ClientAction action) {
		this.points = points;
		this.action = action;
	}

	@Override
	public List<Point> getPoints() {
		return points;
	}

	@Override
	public int size() {
		return points.size();
	}

	public ClientAction getAction() {
		return action;
	}
}
