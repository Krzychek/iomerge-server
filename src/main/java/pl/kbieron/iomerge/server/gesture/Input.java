package pl.kbieron.iomerge.server.gesture;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.MIN_POINTS;


class Input {

	private final List<Point> points;

	private Input(List<Point> points) {
		this.points = points;
	}

	static Builder builder(Normalizer normalizer) {
		return new Builder().withNormalizer(normalizer);
	}

	List<Point> getPoints() {
		return points;
	}

	static class Builder {

		private final List<Point> pointList = new LinkedList<>();

		private Point lastPoint = new Point(0, 0);

		private Normalizer normalizer;

		private Builder() {}

		Input build() {
			return isEnough() ? new Input(normalizer.normalize(pointList)) : null;
		}

		boolean isEnough() {
			return pointList.size() >= MIN_POINTS;
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