package pl.kbieron.iomerge.plugins.server.gesture;


import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.network.MessageDispatcher;

import javax.swing.Timer;


/**
 * Records gesture on mouse press
 * NOTE: Probably whole module would be removed
 */
@Component
class GestureRecorder {

	private final TemplateMatcher templateMatcher;
	private final MessageDispatcher messageDispatcher;
	private final Normalizer normalizer;

	private boolean enoughTime = false;
	private Input.Builder inputBuilder;

	GestureRecorder(Normalizer normalizer, MessageDispatcher messageDispatcher, TemplateMatcher templateMatcher) {
		this.normalizer = normalizer;
		this.messageDispatcher = messageDispatcher;
		this.templateMatcher = templateMatcher;
	}

	synchronized void move(int dx, int dy) {
		if (inputBuilder != null) {
			inputBuilder.move(dx, dy);
		} else {
			Logger.warn("not recording");
		}
	}


	synchronized void startGesture() {
		inputBuilder = new Input.Builder().withNormalizer(normalizer);

		enoughTime = false;
		Timer timer = new Timer(200, actionEvent -> enoughTime = true);
		timer.setRepeats(false);
		timer.start();
	}

	synchronized void finishGesture() {
		if (enoughTime && inputBuilder.isEnough()) {
			Input input = inputBuilder.build();
			TemplateMatcher.MatchResult match = templateMatcher.bestMatch(input);

			if (match.getProbability() > Constants.PROB_THRESHOLD) {
				messageDispatcher.dispatchCustomMsg(match.getPattern().getAction());
			}

			Logger.info(String.format("Best gesture match: %s at %2d%%", //
					match.getPattern().getName(), //
					(int) (match.getProbability() * 100)));
		} else {
			Logger.info("Gesture too short");
		}
		inputBuilder = null;
	}
}
