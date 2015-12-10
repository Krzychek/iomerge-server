package pl.kbieron.iomerge.server.gesture.calc;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.stereotype.Component;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static pl.kbieron.iomerge.server.gesture.Constants.NORM_SIZE;


@Component
public class Normalizer {

	private static SplineInterpolator splineInterpolator = new SplineInterpolator();

	public void normalizeDimensions(List<Point> points) {
		int pointsSize = points.size();

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
		double yTranslate = -min.y * xScale;

		points.parallelStream().forEach(point -> {
			point.x = (int) (point.x * xScale + xTranslate);
			point.y = (int) (point.y * yScale + yTranslate);
		});
	}

	public List<Point> resample(List<Point> points, int size) {
		int inSize = points.size();
		DoubleList xList = new ArrayDoubleList(inSize);
		DoubleList yList = new ArrayDoubleList(inSize);

		IntStream indRange = IntStream.range(0, inSize);
		double[] arguments = indRange.asDoubleStream().toArray();
		points.stream().forEachOrdered(point -> {
			xList.add(point.x);
			yList.add(point.y);
		});

		//noinspection SuspiciousNameCombination
		PolynomialSplineFunction interpolateX = splineInterpolator.interpolate(arguments, xList.toArray());
		PolynomialSplineFunction interpolateY = splineInterpolator.interpolate(arguments, yList.toArray());

		Point[] result = new Point[size];
		for ( int i = 0; i < size; i++ ) {
			result[i] = new Point( //
					(int) interpolateX.value(((double) i) / (inSize - 1)), //
					(int) interpolateY.value(((double) i) / (inSize - 1)));

		}

		return Arrays.asList(result);

	}
}
