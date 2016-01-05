package pl.kbieron.iomerge.server.gesture.calc;

import java.awt.Point;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.A_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.E_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.LAMBDA;


class LikelihoodCalculator {

	public double getLikelihood(List<Point> sequence, List<Point> pattern) {
		MeanDistances d = MeanDistances.getMeanDistances(sequence, pattern);
		return Math.exp(-( //
				LAMBDA * pow(d.euclidean) / pow(E_SIGMA) //
						+ (1.0 - LAMBDA) * pow(d.turningAngle) / pow(A_SIGMA) //
		));
	}

	private double pow(double x) {
		return x * x;
	}

}
