package pl.kbieron.iomerge.server.gesture.calc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Point;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.A_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.E_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.LAMBDA;


@Component
public class LikelihoodCalculator {

	@Autowired
	private Normalizer normalizer;

	public double getLikelihood(List<Point> sequence, List<Point> normSeq) {
		List<Point> resampled = normalizer.resample(sequence, normSeq.size());

		MeanDistances d = MeanDistances.getMeanDistances(resampled, normSeq);
		return Math.exp(-( //
				LAMBDA * pow(d.euclidean) / E_SIGMA //
						+ (1.0 - LAMBDA) * pow(d.turningAngle) / A_SIGMA //
		));
	}

	private double pow(double x) {
		return x * x;
	}

}
