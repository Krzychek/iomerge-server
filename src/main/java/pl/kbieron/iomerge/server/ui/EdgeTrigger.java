package pl.kbieron.iomerge.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.ui.adapters.MouseEnteredAdapter;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;
import javax.swing.JWindow;
import javax.swing.Timer;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Arrays;


@Component
public class EdgeTrigger extends JWindow implements ApplicationListener<AppState.UpdateEvent> {

	@Autowired
	private AppStateManager appStateManager;

	@ConfigProperty( "Edge" )
	private Edge edge = Edge.LEFT;

	@ConfigProperty( "EdgeTriggerOffset" )
	private int offset = 200;

	@ConfigProperty( "EdgeTriggerLength" )
	private int length = 500;

	@PostConstruct
	private void init() {
		UIHelper.makeInvisible(this);

		addMouseListener((MouseEnteredAdapter) e -> appStateManager.enterRemoteScreen());
	}

	@Override
	public void setVisible(boolean visible) {
		if ( visible && isVisible() ) return;
		reposition();
		super.setVisible(visible);
	}

	private void reposition() {
		Rectangle displayRect;
		switch (edge) {
			case LEFT:
				displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
						.map(screen -> screen.getDefaultConfiguration().getBounds()) //
						.min((a, b) -> a.x - b.x) //
						.get();
				displayRect.translate(0, offset);
				break;

			case RIGHT:
			default:
				displayRect = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) //
						.map(screen -> screen.getDefaultConfiguration().getBounds()) //
						.max((a, b) -> a.x - b.x) //
						.get();
				displayRect.translate(displayRect.width - getWidth(), offset);
		}

		setLocation(displayRect.x, displayRect.y);
		setSize(1, length);
	}

	@Override
	public void onApplicationEvent(AppState.UpdateEvent appStateUpdateEvent) {
		boolean visible = AppState.ON_LOCAL == appStateUpdateEvent.getStateChange();
		if ( !visible ) {
			setVisible(false);
		} else {
			Timer timer = new Timer(500, actionEvent -> setVisible(true));
			timer.setRepeats(false);
			timer.start();
		}
	}

	public void setProperties(Edge edge, int length, int offset) {
		if ( edge != this.edge || length != this.length || offset != this.offset ) {
			this.edge = edge;
			this.offset = offset;
			this.length = length;
			reposition();
		}
	}

	public Edge getEdge() {
		return edge;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
}
