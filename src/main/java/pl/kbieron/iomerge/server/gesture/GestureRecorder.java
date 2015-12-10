package pl.kbieron.iomerge.server.gesture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.gesture.calc.Normalizer;
import pl.kbieron.iomerge.server.gesture.calc.TemplateMatcher;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.ui.movementReader.MovementListener;


@Component
public class GestureRecorder implements MovementListener {

	private final Log log = LogFactory.getLog(MovementListener.class);

	@Autowired
	private TemplateMatcher templateMatcher;

	@Autowired
	private Normalizer normalizer;

	private int width;

	private int height;

	private boolean active;

	private Input.Builder inputBuilder;

	@Override
	synchronized public void moveMouse(int dx, int dy) {
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
		Input input = inputBuilder.build();
		if ( input.size() > 5 ) {
			TemplateMatcher.MatchResult match = templateMatcher.bestMatch(input);
			log.info(match.getPattern().getName() + ":" + match.getProbability());

		}
		inputBuilder = null;
	}

}
