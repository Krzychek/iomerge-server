package pl.kbieron.iomerge.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.network.RemoteMsgDispatcher;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.ui.adapters.MouseEnteredAdapter;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Arrays;


@Component
class EdgeTrigger extends JFrame implements ApplicationListener<AppState.UpdateEvent> {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private RemoteMsgDispatcher remoteMsgDispatcher;

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

		boolean wasVisible = isVisible();
		setVisible(false);

		setLocation(displayRect.x, displayRect.y);
		setSize(1, length);

		setVisible(wasVisible);
	}

	@Override
	public void onApplicationEvent(AppState.UpdateEvent appStateUpdateEvent) {
		AppState stateChange = appStateUpdateEvent.getStateChange();
		if ( AppState.ON_LOCAL == stateChange ) {
			remoteMsgDispatcher.dispatchEdgeSync(edge);
			reposition();
			Timer timer = new Timer(50, actionEvent -> setVisible(true));
			timer.setRepeats(false);
			timer.start();
		} else {
			setVisible(false);
		}
	}

	public void setProperties(Edge edge, int length, int offset) {
		if ( edge != this.edge || length != this.length || offset != this.offset ) {
			this.edge = edge;
			this.offset = offset;
			this.length = length;
			reposition();
			remoteMsgDispatcher.dispatchEdgeSync(edge);
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
