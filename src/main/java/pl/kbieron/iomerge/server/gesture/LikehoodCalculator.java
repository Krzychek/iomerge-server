package pl.kbieron.iomerge.server.gesture;

import java.awt.Point;
import java.util.List;


public class LikehoodCalculator {

	private double lambda;

	public LikehoodCalculator(double lambda, double eukledianVarience) {
		if ( !(lambda >= 0.0 && lambda <= 1.0) )
			throw new IllegalArgumentException("lambda should be number in range (0,1)");

		this.lambda = lambda;
	}

	public double getLikelihood(Input input, Template template) {
		return 0; //TODO
	}

	private double getDistance(List<Point> sequence1, List<Point> sequence2) {
		MeanDistances meanDistances = MeanDistances.getMeanDistances(sequence1, sequence2);

		return Math.exp(-(lambda * meanDistances.euclidean + (1.0 - lambda) * meanDistances.turningAngle));
		// TODO podziel przez sigmy!
	}

}
