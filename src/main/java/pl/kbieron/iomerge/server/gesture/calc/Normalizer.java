package pl.kbieron.iomerge.server.gesture.calc;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.springframework.stereotype.Component;

import java.awt.Point;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.awt.geom.Point2D.distance;
import static pl.kbieron.iomerge.server.gesture.Constants.NORM_LENGTH;
import static pl.kbieron.iomerge.server.gesture.Constants.NORM_SIZE;


@Component
public class Normalizer {

	private static SplineInterpolator splineInterpolator = new SplineInterpolator();

	public void normalizeDimensions(List<Point> points) {
		Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

		for ( Point point : points ) {
			if ( point.x < min.x ) min.x = point.x;
			if ( point.x > max.x ) max.x = point.x;
			if ( point.y < min.y ) min.y = point.y;
			if ( point.y > max.y ) max.y = point.y;
		}

		double xScale = NORM_SIZE / (max.x - min.x);
		double xTranslate = -min.x * xScale;
		double yScale = NORM_SIZE / (max.y - min.y);
		double yTranslate = -min.y * yScale;

		points.parallelStream().forEach(point -> {
			point.x = (int) (point.x * xScale + xTranslate);
			point.y = (int) (point.y * yScale + yTranslate);
		});
	}

	public List<Point> resample(List<Point> points) {

		double lengthSum = 0.0;
		Iterator<Point> iterator = points.iterator();
		Point prevPt = iterator.next();
		while ( iterator.hasNext() ) {
			Point nextPt = iterator.next();
			lengthSum += distance(prevPt.x, prevPt.x, nextPt.x, nextPt.x);
			prevPt = nextPt;
		}

		double scale = NORM_LENGTH / lengthSum;

		iterator = points.iterator();
		Point[] result = new Point[NORM_LENGTH];

		double prevLength = 0.0, length = 0.0;
		Point previous = iterator.next(), point = null;

		int ind = 0;
		while ( ind < NORM_LENGTH ) {
			if ( iterator.hasNext() ) {
				point = iterator.next();

				double dist = scale * distance(previous.x, previous.x, point.x, point.x);
				length += dist;
				while ( ind < length && ind < NORM_LENGTH ) {
					double diff = ind - prevLength;
					double percent = diff / dist;
					result[ind++] = new Point( //
							(int) (previous.x + (percent * (point.x - previous.x))), //
							(int) (previous.y + (percent * point.y - previous.y)));
				}

				previous = point;
				prevLength = length;
			} else {
				prevLength = length;
				break;
			}
		}
		if ( point != null ) {
			double dist = scale * distance(previous.x, previous.x, point.x, point.x);
			while ( ind < NORM_LENGTH ) {
				double diff = ind - prevLength;
				double percent = diff / dist;
				result[ind++] = new Point( //
						(int) (previous.x + (percent * point.x)), //
						(int) (previous.y + (percent * point.y)));
			}
		}

		return Arrays.asList(result);
	}

}
