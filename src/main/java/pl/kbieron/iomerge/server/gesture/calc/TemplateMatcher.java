package pl.kbieron.iomerge.server.gesture.calc;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kbieron.iomerge.server.gesture.PatternDatabase;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.gesture.model.Pattern;


public class TemplateMatcher {

	@Autowired
	private LikelihoodCalculator likelihoodCalculator;

	@Autowired
	private PatternDatabase patternDatabase;

	public MatchResult bestMatch(Input input) {
		return patternDatabase.getPatterns().parallelStream() //
				.map(pattern -> new MatchResult(pattern, //
						likelihoodCalculator.getLikelihood(input.getPoints(), pattern.getPoints())))
				.max(MatchResult::compareTo).get();
	}

	public static class MatchResult implements Comparable<MatchResult> {

		private final double probability;

		private final Pattern pattern;

		public MatchResult(Pattern pattern, double probability) {
			this.pattern = pattern;
			this.probability = probability;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public double getProbability() {
			return probability;
		}

		@Override
		@SuppressWarnings( "NullableProblems" )
		public int compareTo(MatchResult that) {
			return Double.compare(this.probability, that.probability);
		}
	}
}
