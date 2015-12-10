package pl.kbieron.iomerge.server.gesture.calc;

import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.gesture.model.Template;

import java.awt.Point;
import java.util.List;

import static pl.kbieron.iomerge.server.gesture.Constants.A_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.E_SIGMA;
import static pl.kbieron.iomerge.server.gesture.Constants.LAMBDA;


@Component
public class LikelihoodCalculator {

	public double getLikelihood(Input input, Template template) {
		return 0; //TODO
	}

	private double getDistance(List<Point> sequence1, List<Point> sequence2) {
		MeanDistances d = MeanDistances.getMeanDistances(sequence1, sequence2);

		return Math.exp(-( //
				LAMBDA * pow(d.euclidean) / E_SIGMA //
						+ (1.0 - LAMBDA) * pow(d.turningAngle) / A_SIGMA //
		));
	}

	private double pow(double x) {
		return x * x;
	}

}
