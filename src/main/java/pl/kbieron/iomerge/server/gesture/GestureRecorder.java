package pl.kbieron.iomerge.server.gesture;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.movementReader.MovementListener;
import pl.kbieron.iomerge.server.network.MsgDispatcher;

import javax.swing.*;


/**
 * Records gesture on mouse press
 * NOTE: Probably whole module would be removed
 */
@Component
public class GestureRecorder implements MovementListener {

	private final Logger log = Logger.getLogger(MovementListener.class);

	@Autowired
	private TemplateMatcher templateMatcher;

	@Autowired
	private MsgDispatcher msgDispatcher;

	@Autowired
	private Normalizer normalizer;

	private boolean enoughTime = false;

	private Input.Builder inputBuilder;

	GestureRecorder() {}

	@Override
	synchronized public void move(int dx, int dy) {
		if ( inputBuilder != null ) {
			inputBuilder.move(dx, dy);
		} else {
			log.warn("not recording");
		}
	}

	@Override
	synchronized public void mousePressed() {
		inputBuilder = Input.builder(normalizer);

		enoughTime = false;
		Timer timer = new Timer(200, actionEvent -> enoughTime = true);
		timer.setRepeats(false);
		timer.start();
	}

	@Override
	synchronized public void mouseReleased() {
		if ( enoughTime && inputBuilder.isEnough() ) {
			Input input = inputBuilder.build();
			TemplateMatcher.MatchResult match = templateMatcher.bestMatch(input);

			if ( match.getProbability() > Constants.PROB_THRESHOLD ) {
				msgDispatcher.dispatchCustomMsg(match.getPattern().getAction());
			}

			log.info(String.format("Best gesture match: %s at %2d%%", //
					match.getPattern().getName(), //
					(int) (match.getProbability() * 100)));
		} else {
			log.info("Gesture too short");
		}
		inputBuilder = null;
	}
}
