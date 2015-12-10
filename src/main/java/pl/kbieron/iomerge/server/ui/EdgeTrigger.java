package pl.kbieron.iomerge.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static pl.kbieron.iomerge.server.utilities.Edge.LEFT;


@Component
public class EdgeTrigger extends InvisibleJFrame {

	@Autowired
	private Director director;

	@ConfigProperty
	private Edge edge = LEFT;

	@ConfigProperty
	private Integer offset = 0;

	@ConfigProperty
	private Integer length = 100;

	@ConfigProperty
	private Integer thickness = 5;

	public EdgeTrigger() {
		super("Edge Trigger");
	}

	@PostConstruct
	public void init() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
				director.enterRemoteScreen(mouseEvent.getY() / getHeight());
			}
		});
	}

	public void reposition() {
		Rectangle displayRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration().getBounds();

		if ( edge == LEFT ) {
			setSize(thickness, length);
			setLocation(displayRect.x, displayRect.y + offset);
		} else {
			setSize(thickness, length);
			setLocation(displayRect.x + displayRect.width - getWidth(), displayRect.y + offset);
		}
	}

	public void releaseMouse(double yScale) {
		Point location = getLocation();
		try {
			if ( edge == LEFT ) {
				new Robot().mouseMove(location.x - thickness, (int) yScale * getHeight() + location.y);
			} else {
				new Robot().mouseMove(location.x, (int) yScale * getHeight() + location.y);
			}
		} catch (AWTException e) {
			e.printStackTrace(); // TODO
		}

	}

	public EdgeTrigger setThickness(int thickness) {
		this.thickness = thickness;
		return this;
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
}
