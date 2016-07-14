package pl.kbieron.iomerge.plugins.server.gesture;

import org.springframework.stereotype.Component;


@Component
class TemplateMatcher {

	private final LikelihoodCalculator likelihoodCalculator;

	private final PatternDatabase patternDatabase;

	public TemplateMatcher(PatternDatabase patternDatabase, LikelihoodCalculator likelihoodCalculator) {
		this.patternDatabase = patternDatabase;
		this.likelihoodCalculator = likelihoodCalculator;
	}

	MatchResult bestMatch(Input input) {
		return patternDatabase.getPatterns().parallelStream() //
				.map(pattern -> new MatchResult(pattern, //
						likelihoodCalculator.getLikelihood(input.getPoints(), pattern.getPoints())))
				.max((o1, o2) -> Double.compare(o1.probability, o2.probability))
				.orElseThrow(() -> new IllegalStateException("no patterns in db"));
	}

	static class MatchResult {

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
	}
}
