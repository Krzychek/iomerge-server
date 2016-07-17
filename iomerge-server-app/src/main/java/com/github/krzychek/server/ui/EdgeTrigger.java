package com.github.krzychek.server.ui;

import com.github.krzychek.server.api.appState.AppState;
import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.api.network.MessageDispatcher;
import com.github.krzychek.server.model.Edge;
import org.annoprops.annotations.ConfigProperty;
import org.annoprops.annotations.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.util.Arrays;


/**
 * Enters remote device on mouse enter. displayed as 1px on border of screen
 */
@PropertyHolder
@Component
public class EdgeTrigger extends JFrame {

	private static final String DIMENSION_EXCEPTION = "Problem with getting display dimension";

	private final AppStateManager appStateManager;
	private final MessageDispatcher messageDispatcher;

	@ConfigProperty("Edge")
	private Edge edge = Edge.LEFT;

	@ConfigProperty("EdgeTriggerOffset")
	private int offset = 200;

	@ConfigProperty("EdgeTriggerLength")
	private int length = 500;

	@Autowired
	EdgeTrigger(MessageDispatcher messageDispatcher, AppStateManager appStateManager) throws HeadlessException {
		super("IOMerge Trigger");

		this.messageDispatcher = messageDispatcher;
		this.appStateManager = appStateManager;

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

	@EventListener
	public void onStateChange(AppState appStateUpdateEvent) {
		if (AppState.ON_LOCAL == appStateUpdateEvent) {
			messageDispatcher.dispatchEdgeSync(edge);
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
			messageDispatcher.dispatchEdgeSync(edge);
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