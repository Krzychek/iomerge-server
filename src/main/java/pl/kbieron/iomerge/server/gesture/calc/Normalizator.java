package pl.kbieron.iomerge.server.gesture.calc;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.NORM_LENGTH;
import static pl.kbieron.iomerge.server.gesture.Constants.NORM_SIZE;


public class Normalizator {

	private static SplineInterpolator splineInterpolator = new SplineInterpolator();

	public static List<Point> normalize(List<Point> points) {
		int pointsSize = points.size();

		Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

		ArrayDoubleList xNotNorm = new ArrayDoubleList(pointsSize);
		ArrayDoubleList yNotNorm = new ArrayDoubleList(pointsSize);

		for ( Point point : points ) {
			if ( point.x < min.x ) min.x = point.x;
			if ( point.x > max.x ) max.x = point.x;
			if ( point.y < min.y ) min.y = point.y;
			if ( point.y > max.y ) max.y = point.y;
			xNotNorm.add(point.x);
			yNotNorm.add(point.y);
		}

		double xScale = NORM_SIZE / (max.x - min.x);
		double xTranslate = -min.x * xScale;
		double yScale = NORM_SIZE / (max.y - min.y);
		double yTranslate = -min.y * xScale;

		double[] xArr = xNotNorm.toArray();
		double[] yArr = yNotNorm.toArray();
		double[] index = new double[pointsSize];
		for ( int i = 0; i < pointsSize; i++ ) {
			index[i] = i;
			xArr[i] = xArr[i] * xScale + xTranslate;
			yArr[i] = yArr[i] * yScale + yTranslate;
		}

		//noinspection SuspiciousNameCombination
		PolynomialSplineFunction interpolateX = splineInterpolator.interpolate(index, xArr);
		PolynomialSplineFunction interpolateY = splineInterpolator.interpolate(index, yArr);

		Point[] result = new Point[NORM_LENGTH];
		for ( int i = 0; i < NORM_LENGTH; i++ ) {
			result[i] = new Point( //
					(int) interpolateX.value(((double) i) / (pointsSize - 1)), //
					(int) interpolateY.value(((double) i) / (pointsSize - 1)));

		}

		return Arrays.asList(result);
	}
}
