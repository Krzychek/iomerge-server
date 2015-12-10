package pl.kbieron.iomerge.server.gesture.model;

import pl.kbieron.iomerge.server.gesture.calc.Normalizator;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Input implements Template, Serializable {

	private List<Point> allSegments;

	private Input(List<Point> points) {
		this.allSegments = points;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public List<Point> getSegment(int i) {
		return allSegments.subList(0, i);
	}

	@Override
	public List<Point> getFullSegment() {
		return allSegments;
	}

	public static class Builder {

		private List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		private Builder() {}

		public Input build() {
			List<Point> normalPts = Normalizator.normalize(pointList);
			return new Input(normalPts);
		}

		public void move(int dx, int dy) {
			Point point = new Point(lastPoint.x + dx, lastPoint.y + dy);
			lastPoint = point;
			pointList.add(point);
		}
	}
}