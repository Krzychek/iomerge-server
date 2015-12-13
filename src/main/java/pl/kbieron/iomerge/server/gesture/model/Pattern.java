package pl.kbieron.iomerge.server.gesture.model;

import pl.kbieron.iomerge.model.RemoteActionType;

import java.awt.Point;
import java.util.List;


public class Pattern implements Template {

	List<Point> points;

	private RemoteActionType action;

	public Pattern(List<Point> points, RemoteActionType action) {
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

	public RemoteActionType getAction() {
		return action;
	}
}
