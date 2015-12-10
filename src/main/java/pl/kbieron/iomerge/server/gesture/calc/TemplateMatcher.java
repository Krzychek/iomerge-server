package pl.kbieron.iomerge.server.gesture.calc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.gesture.model.Pattern;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;


@Component
public class TemplateMatcher {

	@Autowired
	LikelihoodCalculator likelihoodCalculator;

	@Autowired
	Normalizer normalizer;

	List<Pattern> patterns = Arrays.asList( //
			new Pattern(Arrays.asList( //
					new Point(0, 0), //
					new Point(50, 0), //
					new Point(100, 0), //
					new Point(150, 0), //
					new Point(200, 0), //
					new Point(250, 0), //
					new Point(300, 0), //
					new Point(350, 0), //
					new Point(400, 0), //
					new Point(450, 0), //
					new Point(500, 0), //
					new Point(550, 0), //
					new Point(600, 0), //
					new Point(650, 0), //
					new Point(700, 0), //
					new Point(750, 0), //
					new Point(800, 0), //
					new Point(850, 0), //
					new Point(900, 0), //
					new Point(950, 0)), "w prawo"), //
			new Pattern(Arrays.asList( //
					new Point(950, 0), //
					new Point(900, 0), //
					new Point(850, 0), //
					new Point(800, 0), //
					new Point(750, 0), //
					new Point(700, 0), //
					new Point(650, 0), //
					new Point(600, 0), //
					new Point(550, 0), //
					new Point(500, 0), //
					new Point(450, 0), //
					new Point(400, 0), //
					new Point(350, 0), //
					new Point(300, 0), //
					new Point(250, 0), //
					new Point(200, 0), //
					new Point(150, 0), //
					new Point(100, 0), //
					new Point(50, 0), //
					new Point(0, 0)), "w prawo"), //
			new Pattern(Arrays.asList( //
					new Point(65, 0), //
					new Point(179, 0), //
					new Point(294, 0), //
					new Point(408, 3), //
					new Point(523, 13), //
					new Point(637, 30), //
					new Point(752, 59), //
					new Point(867, 151), //
					new Point(918, 108), //
					new Point(901, 525), //
					new Point(786, 151), //
					new Point(672, 866), //
					new Point(557, 799), //
					new Point(443, 489), //
					new Point(328, 775), //
					new Point(213, 534), //
					new Point(99, 734), //
					new Point(15, 269), //
					new Point(129, 292), //
					new Point(244, 238)), "koło") //
	);

	public MatchResult bestMatch(Input input) {

		return patterns.parallelStream() //
				.map(pattern -> new MatchResult(pattern, //
						likelihoodCalculator.getLikelihood(input.getPoints(), pattern.getPoints())))
				.max(MatchResult::compareTo).get();
	}

	public static class MatchResult implements Comparable<MatchResult> {

		private double probability;

		private Pattern pattern;

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
