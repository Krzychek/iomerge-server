package pl.kbieron.iomerge.server.gesture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.calc.Normalizer;
import pl.kbieron.iomerge.server.gesture.calc.TemplateMatcher;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.utilities.MovementListener;


@Component
public class GestureRecorder implements MovementListener {

	private final Log log = LogFactory.getLog(MovementListener.class);

	@Autowired
	private TemplateMatcher templateMatcher;

	@Autowired
	private EventServer eventServer;

	@Autowired
	private Normalizer normalizer;

	private int width;

	private int height;

	private boolean active;

	private Input.Builder inputBuilder;

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
	}

	@Override
	synchronized public void mouseReleased() {
		if ( inputBuilder.isEnough() ) {
			Input input = inputBuilder.build();
			TemplateMatcher.MatchResult match = templateMatcher.bestMatch(input);

			if ( match.getProbability() > Constants.PROB_THRESHOLD ) {
				eventServer.sendToClient(match.getPattern().getAction());
			}

			log.info(String.format("Best match: %s at %2d%%", //
					match.getPattern().getName(), //
					(int) (match.getProbability() * 100)));
		} else {
			log.info("gesture too short");
		}
		inputBuilder = null;
	}

}
