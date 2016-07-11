package pl.kbieron.iomerge.server.plugins.gesture;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.awt.geom.Point2D.distance;
import static pl.kbieron.iomerge.server.plugins.gesture.Constants.MIN_POINTS;
import static pl.kbieron.iomerge.server.plugins.gesture.Constants.NORM_LENGTH;
import static pl.kbieron.iomerge.server.plugins.gesture.Constants.NORM_SIZE;


@Component
class Normalizer {

	List<Point> normalize(List<Point> points) {
		if (points.size() < MIN_POINTS) throw new IllegalArgumentException("too few points provided");

		double lengthSum = 0.0;
		// resample
		Iterator<Point> iter = points.iterator();
		Point prevPt = iter.next();
		Point nextPt;
		while (iter.hasNext()) {
			nextPt = iter.next();
			lengthSum += distance(prevPt.x, prevPt.y, nextPt.x, nextPt.y);
			prevPt = nextPt;
		}

		double lengthScale = NORM_LENGTH / lengthSum;

		Iterator<Point> iterator = points.iterator();
		Point[] result = new Point[NORM_LENGTH];

		double prevLength = 0.0;
		double length = 0.0;
		Point prevPoint = iterator.next(), lastPoint = null;

		int ind = 0;
		while (ind < NORM_LENGTH) {
			if (iterator.hasNext()) {
				lastPoint = iterator.next();

				double dist = lengthScale * distance(prevPoint.x, prevPoint.y, lastPoint.x, lastPoint.y);
				length += dist;
				while (ind < length && ind < NORM_LENGTH) {
					double diff = ind - prevLength;
					double percent = diff / dist;
					result[ind++] = new Point( //
							(int) (prevPoint.x + (percent * (lastPoint.x - prevPoint.x))), //
							(int) (prevPoint.y + (percent * lastPoint.y - prevPoint.y)));
				}

				prevPoint = lastPoint;
				prevLength = length;
			} else {
				prevLength = length;
				break;
			}
		}

		//noinspection ConstantConditions
		double dist = lengthScale * distance(prevPoint.x, prevPoint.y, lastPoint.x, lastPoint.y);

		while (ind < NORM_LENGTH) {
			double diff = ind - prevLength;
			double percent = diff / dist;
			result[ind] = new Point( //
					(int) (prevPoint.x + (percent * lastPoint.x)), //
					(int) (prevPoint.y + (percent * lastPoint.y)));
			ind++;
		}

		rescale(result);

		return Arrays.asList(result);
	}

	private void rescale(Point[] result) {//get max of resampled
		Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

		for (Point pt : result) {
			if (pt.x < min.x) min.x = pt.x;
			if (pt.x > max.x) max.x = pt.x;
			if (pt.y < min.y) min.y = pt.y;
			if (pt.y > max.y) max.y = pt.y;
		}

		// rescale
		double xScale = NORM_SIZE / (max.x - min.x);
		double yScale = NORM_SIZE / (max.y - min.y);
		double scale = Math.min(xScale, yScale);

		double xTranslate = -min.x * scale;
		double yTranslate = -min.y * scale;

		for (Point point : result) {
			point.x = (int) (point.x * scale + xTranslate);
			point.y = (int) (point.y * scale + yTranslate);
		}
	}

}
