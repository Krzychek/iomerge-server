package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.util.List;


public class Pattern implements Template {

	List<Point> points;

	private String name;

	public Pattern(List<Point> points, String name) {
		this.points = points;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public List<Point> getPoints() {
		return points;
	}

	@Override
	public int size() {
		return points.size();
	}
}
