package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public class Pattern implements Template {

	List<Point> points;

	private byte action;

	private String name;

	public Pattern(List<Point> points, byte action, String name) {
		this.points = points;
		this.action = action;
		this.name = name;
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

	public String getName() {
		return name;
	}
}
