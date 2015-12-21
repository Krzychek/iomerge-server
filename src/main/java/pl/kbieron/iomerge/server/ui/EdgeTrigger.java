package pl.kbieron.iomerge.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.appState.StateObserver;
import pl.kbieron.iomerge.server.appState.StateType;
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
public class EdgeTrigger extends JWindow implements StateObserver {

	@Autowired
	private AppStateManager appStateManager;

	@ConfigProperty( "Edge" )
	private Edge edge = Edge.LEFT;

	@ConfigProperty( "EdgeTriggerOffset" )
	private Integer offset = 200;

	@ConfigProperty( "EdgeTriggerLength" )
	private Integer length = 500;

	@PostConstruct
	private void init() {
		UIHelper.makeInvisible(this);

		addMouseListener((MouseEnteredAdapter) e -> appStateManager.enterRemoteScreen());
	}

	@Override
	public void update(AppStateManager appStateManager) {
		boolean visible = StateType.ON_LOCAL == appStateManager.getStateChange();
		if ( !visible ) {
			setVisible(false);
		} else {
			Timer timer = new Timer(500, actionEvent -> setVisible(true));
			timer.setRepeats(false);
			timer.start();
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if ( visible && isVisible() ) return;
		reposition();
		super.setVisible(visible);
	}

	private void reposition() {
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
