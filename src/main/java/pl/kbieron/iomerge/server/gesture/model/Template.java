package pl.kbieron.iomerge.server.gesture.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;


public class Template implements Serializable {

	public List<Point> fullSegment;

	public Template(List<Point> points) {
		this.fullSegment = points;
	}

	public List<Point> getSegment(int i) {
		return fullSegment.subList(0, i);
	}
}
