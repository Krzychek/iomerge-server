package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public class Pattern {

	private final List<Point> points;

	private final byte action;

	private final String name;

	public Pattern(List<Point> points, byte action, String name) {
		this.points = points;
		this.action = action;
		this.name = name;
	}

	public List<Point> getPoints() {
		return points;
	}

	public byte getAction() {
		return action;
	}

	public String getName() {
		return name;
	}
}
