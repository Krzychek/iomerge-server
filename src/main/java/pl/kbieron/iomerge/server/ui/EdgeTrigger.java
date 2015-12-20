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
import javax.swing.JWindow;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;


@Component
public class EdgeTrigger extends JWindow implements StateObserver {

	private final Log log = LogFactory.getLog(EdgeTrigger.class);

	@Autowired
	private AppStateManager appStateManager;

	private Edge edge = Edge.LEFT;

	@ConfigProperty
	private Integer offset = 200;

	@ConfigProperty
	private Integer length = 500;

	@PostConstruct
	private void init() {
		UIHelper.makeInvisible(this);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
				appStateManager.enterRemoteScreen(mouseEvent.getY() / getHeight());
			}
		});
		appStateManager.addObserver(this);
	}

	public EdgeTrigger setEdge(Edge edge) {
		this.edge = edge;
		return this;
	}

	public EdgeTrigger setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public EdgeTrigger setLength(int length) {
		this.length = length;
		return this;
	}

	@Override
	public void update(AppStateManager appStateManager) {
		setVisible(StateType.CONNECTED == appStateManager.getStateChange());
	}

	@Override
	public void setVisible(boolean visible) {
		if ( isVisible() ) return;
		reposition();
		setBackground(Color.BLUE);
		super.setVisible(visible);
	}

	public void reposition() {
		if ( edge == Edge.LEFT ) {

			Rectangle displayRect = Arrays
					.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
					.map(screen -> screen.getDefaultConfiguration().getBounds()) //
					.min((a, b) -> a.x - b.x) //
					.get();

			setLocation(displayRect.x, displayRect.y + offset);

		} else {

			Rectangle displayRect = Arrays
					.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
					.map(screen -> screen.getDefaultConfiguration().getBounds()) //
					.max((a, b) -> a.x - b.x) //
					.get();

			setLocation(displayRect.x + displayRect.width - getWidth(), displayRect.y + offset);
		}

		setSize(1, length);
	}

}
