package pl.kbieron.iomerge.plugins.server.gesture;

import pl.kbieron.iomerge.model.message.Message;

import java.awt.Point;
import java.util.List;


class Pattern {

	private final List<Point> points;
	private final Message action;
	private final String name;

	Pattern(List<Point> points, Message action, String name) {
		this.points = points;
		this.action = action;
		this.name = name;
	}

	List<Point> getPoints() {
		return points;
	}

	Message getAction() {
		return action;
	}

	String getName() {
		return name;
	}
}
