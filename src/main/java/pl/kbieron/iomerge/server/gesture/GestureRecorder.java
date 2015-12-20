package pl.kbieron.iomerge.server.gesture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.gesture.calc.Normalizer;
import pl.kbieron.iomerge.server.gesture.calc.TemplateMatcher;
import pl.kbieron.iomerge.server.gesture.model.Input;
import pl.kbieron.iomerge.server.network.RemoteMsgDispatcher;
import pl.kbieron.iomerge.server.utilities.MovementListener;

import javax.swing.Timer;


@Component
public class GestureRecorder implements MovementListener {

	private final Log log = LogFactory.getLog(MovementListener.class);

	@Autowired
	private TemplateMatcher templateMatcher;

	@Autowired
	private RemoteMsgDispatcher remoteMsgDispatcher;

	@Autowired
	private Normalizer normalizer;

	@Autowired
	private AppStateManager appStateManager;

	private boolean enoughTime = false;

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
				remoteMsgDispatcher.dispatchCustomMsg(match.getPattern().getAction());
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
