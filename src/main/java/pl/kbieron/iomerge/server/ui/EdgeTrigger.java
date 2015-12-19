package pl.kbieron.iomerge.server.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.appState.StateObserver;
import pl.kbieron.iomerge.server.appState.StateType;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class EdgeTrigger extends InvisibleJWindow implements StateObserver {

	private final Log log = LogFactory.getLog(InvisibleJWindow.class);

	@Autowired
	private AppStateManager appAppStateManager;

	private Edge edge = Edge.LEFT;

	@ConfigProperty
	private Integer offset = 200;

	@ConfigProperty
	private Integer length = 500;

	@PostConstruct
	private void init() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
				appAppStateManager.enterRemoteScreen(mouseEvent.getY() / getHeight());
			}
		});
	}

	public InvisibleJWindow setEdge(Edge edge) {
		this.edge = edge;
		return this;
	}

	public InvisibleJWindow setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public InvisibleJWindow setLength(int length) {
		this.length = length;
		return this;
	}

	@Override
	public void update(AppStateManager appStateManager) {
		setVisible(StateType.CONNECTED == appStateManager.getStateType());
	}

	@Override
	public void setVisible(boolean visible) {
		if ( !isVisible() ) reposition();
		setBackground(Color.BLUE);
		super.setVisible(visible);
	}

	public void reposition() {
		Rectangle displayRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration().getBounds();

		setSize(1, length);
		if ( edge == Edge.LEFT ) {
			setLocation(displayRect.x, displayRect.y + offset);
		} else {
			setLocation(displayRect.x + displayRect.width - getWidth(), displayRect.y + offset);
		}
	}

}
