package pl.kbieron.iomerge.plugins.server.gesture;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;


class Input {

	private final List<Point> points;

	private Input(List<Point> points) {
		this.points = points;
	}

	List<Point> getPoints() {
		return points;
	}

	static class Builder {

		private final List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		private Normalizer normalizer;

		Builder() {}

		Input build() {
			return isEnough() ? new Input(normalizer.normalize(pointList)) : null;
		}

		boolean isEnough() {
			return pointList.size() >= Constants.MIN_POINTS;
		}

		void move(int dx, int dy) {
			Point point = new Point(lastPoint.x + dx, lastPoint.y + dy);
			lastPoint = point;
			pointList.add(point);
		}

		Builder withNormalizer(Normalizer normalizer) {
			this.normalizer = normalizer;
			return this;
		}
	}
}