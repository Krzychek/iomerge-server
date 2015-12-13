package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public class Pattern implements Template {

	List<Point> points;

	private byte action;

	public Pattern(List<Point> points, byte action) {
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

	public byte getAction() {
		return action;
	}
}
