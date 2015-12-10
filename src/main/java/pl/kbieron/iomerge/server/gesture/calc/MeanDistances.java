package pl.kbieron.iomerge.server.gesture.calc;

import java.awt.Point;
import java.util.List;


class MeanDistances {

	public double euclidean;

	public double turningAngle;

	private MeanDistances(double euclidean, double turningAngle) {
		this.euclidean = euclidean;
		this.turningAngle = turningAngle;
	}

	public static MeanDistances getMeanDistances(List<Point> seq1, List<Point> seq2) {
		int length = seq1.size();
		double euclideanSum = 0.0, turningAngleSum = 0.0;

		double distancePrev = seq1.get(0).distance(seq2.get(0));
		Point point1Prev = seq1.get(0), point2Prev = seq2.get(0);

		euclideanSum += distancePrev;

		for ( int i = 1; i < length; ++i ) {
			Point point1 = seq1.get(i), point2 = seq2.get(i);

			double distance = point1.distance(point2);

			euclideanSum += distance;
			turningAngleSum += getTurningAngle(point1, point1Prev, point2, point2Prev, distance, distancePrev);

			point1Prev = point1;
			point2Prev = point2;
			distancePrev = distance;
		}

		return new MeanDistances(euclideanSum / length, turningAngleSum / length);
	}

	private static double getTurningAngle(Point a1, Point a2, Point b1, Point b2, double distance_a1b1, double distance_a2b2) {
		return Math.acos( //
				((a2.x - a1.x) * (b2.x - b1.x) + (a2.y - a1.y) * (b2.y - b1.y)) //
						/ (distance_a2b2 * distance_a1b1));
	}

}
