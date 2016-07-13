package pl.kbieron.iomerge.plugins.server.gesture;

import org.springframework.stereotype.Component;

import java.awt.Point;
import java.util.List;


@Component
class LikelihoodCalculator {

	double getLikelihood(List<Point> sequence, List<Point> pattern) {
		MeanDistances d = MeanDistances.getMeanDistances(sequence, pattern);
		return Math.exp(-( //
				Constants.LAMBDA * pow(d.euclidean) / pow(Constants.E_SIGMA) //
				+ (1.0 - Constants.LAMBDA) * pow(d.turningAngle) / pow(Constants.A_SIGMA) //
		));
	}

	private double pow(double x) {
		return x * x;
	}

}
