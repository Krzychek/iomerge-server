package pl.kbieron.iomerge.server.gesture.model;

import pl.kbieron.iomerge.server.gesture.calc.Normalizer;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.MIN_POINTS;


public class Input implements Template, Serializable {

	private List<Point> points;

	private Input(List<Point> points) {
		this.points = points;
	}

	public static Builder builder(Normalizer normalizer) {
		return new Builder().withNormalizer(normalizer);
	}

	@Override
	public List<Point> getPoints() {
		return points;
	}

	@Override
	public int size() {
		return points.size();
	}

	public static class Builder {

		private List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		private Normalizer normalizer;

		private Builder() {}

		public Input build() {
			return isEnough() ? new Input(normalizer.normalize(pointList)) : null;
		}

		public boolean isEnough() {
			return pointList.size() >= MIN_POINTS;
		}

		public void move(int dx, int dy) {
			Point point = new Point(lastPoint.x + dx, lastPoint.y + dy);
			lastPoint = point;
			pointList.add(point);
		}

		public Builder withNormalizer(Normalizer normalizer) {
			this.normalizer = normalizer;
			return this;
		}
	}
}