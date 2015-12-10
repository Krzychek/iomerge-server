package pl.kbieron.iomerge.server.gesture.calc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.alghorithm.MatchResult;
import pl.kbieron.iomerge.server.gesture.model.Template;

import java.awt.Point;
import java.util.List;


@Component
public class TemplateMatcher {

	@Autowired
	LikelihoodCalculator likelihoodCalculator;

	@Autowired
	Normalizer normalizer;

	private Object getIncrementalResult(List<Point> unkPts, Template pattern) {
		normalizer.normalizeDimensions(unkPts);
		double maxProb = 0.0d;
		int maxIndex = -1;

		for ( List<Point> points : pattern ) {

			int samplingPtCount = points.size();
			List<Point> resampledPts = normalizer.resample(unkPts, samplingPtCount);
			double prob = likelihoodCalculator.getLikelihood(resampledPts, points);
			if ( prob > maxProb ) {
				maxProb = prob;
				maxIndex = samplingPtCount;
			}
		}
		return new MatchResult(pattern, maxProb, maxIndex);
	}
}
