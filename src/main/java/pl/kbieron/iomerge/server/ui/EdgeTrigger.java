package pl.kbieron.iomerge.server.ui;

import org.annoprops.annotations.ConfigProperty;
import org.annoprops.annotations.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.network.MsgDispatcher;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


/**
 * Enters remote device on mouse enter. displayed as 1px on border of screen
 */
@PropertyHolder
@Component
public class EdgeTrigger extends JFrame implements AppStateListener {

	private static final String DIMENSION_EXCEPTION = "Problem with getting display dimension";

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private MsgDispatcher msgDispatcher;

	@ConfigProperty("Edge")
	private Edge edge = Edge.LEFT;

	@ConfigProperty("EdgeTriggerOffset")
	private int offset = 200;

	@ConfigProperty("EdgeTriggerLength")
	private int length = 500;

	EdgeTrigger() throws HeadlessException {
		super("IOMerge Trigger");
		init();
	}

	private void init() {
		reposition();
		UIHelper.makeInvisible(this);

		addMouseListener((MouseEnteredAdapter) e -> appStateManager.enterRemoteScreen());
	}

	private void reposition() {
		Rectangle displayRect;
		switch (edge) {
			case LEFT:
				displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
						.map(screen -> screen.getDefaultConfiguration().getBounds()) //
						.min((a, b) -> a.x - b.x) //
						.orElseThrow(() -> new IllegalStateException(DIMENSION_EXCEPTION));

				displayRect.translate(0, offset);
				break;

			case RIGHT:
			default:
				displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
						.map(screen -> screen.getDefaultConfiguration().getBounds()) //
						.max((a, b) -> a.x - b.x) //
						.orElseThrow(() -> new IllegalStateException(DIMENSION_EXCEPTION));
				displayRect.translate(displayRect.width - getWidth(), offset);
		}

		boolean wasVisible = isVisible();
		setVisible(false);

		setLocation(displayRect.x, displayRect.y);
		setSize(1, length);

		setVisible(wasVisible);
	}

	@Override
	public void onStateChange(AppState appStateUpdateEvent) {
		if (AppState.ON_LOCAL == appStateUpdateEvent) {
			msgDispatcher.dispatchEdgeSync(edge);
			reposition();
			Timer timer = new Timer(50, actionEvent -> setVisible(true));
			timer.setRepeats(false);
			timer.start();
		} else {
			setVisible(false);
		}
	}

	void setProperties(Edge edge, int length, int offset) {
		if (edge != this.edge || length != this.length || offset != this.offset) {
			this.edge = edge;
			this.offset = offset;
			this.length = length;
			reposition();
			msgDispatcher.dispatchEdgeSync(edge);
		}
	}

	Edge getEdge() {
		return edge;
	}

	int getOffset() {
		return offset;
	}

	int getLength() {
		return length;
	}
}
