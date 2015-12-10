package pl.kbieron.iomerge.server.gesture;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Input implements Serializable {

	private List<Point> allSegments;

	private Input(List<Point> points) {
		this.allSegments = points;
	}

	public List<Point> getSegment(int i) {
		return allSegments.subList(0, i);
	}

	public static class Builder {

		private List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		public Input build() {
			return new Input(new ArrayList<>(pointList));
		}

		public void move(int dx, int dy) {
			Point point = new Point(lastPoint.x + dx, lastPoint.y + dy);
			lastPoint = point;
			pointList.add(point);
		}
	}
}