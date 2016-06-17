package pl.kbieron.iomerge.server.gesture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;


@Component
class TemplateMatcher {

	@Autowired
	private LikelihoodCalculator likelihoodCalculator;

	@Autowired
	private PatternDatabase patternDatabase;

	MatchResult bestMatch(Input input) {
		return patternDatabase.getPatterns().parallelStream() //
				.map(pattern -> new MatchResult(pattern, //
						likelihoodCalculator.getLikelihood(input.getPoints(), pattern.getPoints())))
				.max(MatchResult::compareTo)
				.orElseThrow(() -> new IllegalStateException("no patterns in db"));
	}

	static class MatchResult implements Comparable<MatchResult> {

		private final double probability;

		private final Pattern pattern;

		MatchResult(Pattern pattern, double probability) {
			this.pattern = pattern;
			this.probability = probability;
		}

		Pattern getPattern() {
			return pattern;
		}

		double getProbability() {
			return probability;
		}

		@Override
		public int compareTo(@Nonnull MatchResult that) {
			return Double.compare(this.probability, that.probability);
		}
	}
}
