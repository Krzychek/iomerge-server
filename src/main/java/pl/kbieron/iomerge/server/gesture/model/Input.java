package pl.kbieron.iomerge.server.gesture.model;

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

	@Override
	public int size() {
		return allSegments.size();
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	public static class Builder {

		private List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		private Builder() {}

		public Input build() {
			return new Input(pointList);
		}

		public void move(int dx, int dy) {
			Point point = new Point(lastPoint.x + dx, lastPoint.y + dy);
			lastPoint = point;
			pointList.add(point);
		}
	}


	private class Iterator implements java.util.Iterator<List<Point>> {

		private int i = 0;

		private int lastInd = allSegments.size() - 1;

		private List<Point> segments = allSegments;

		@Override
		public boolean hasNext() {
			return lastInd > i;
		}

		@Override
		public List<Point> next() {
			return segments.subList(0, ++i);
		}
	}
}