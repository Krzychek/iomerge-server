package pl.kbieron.iomerge.server.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.Director;
import pl.kbieron.iomerge.server.properties.ConfigProperty;
import pl.kbieron.iomerge.server.utilities.Edge;

import javax.annotation.PostConstruct;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class EdgeTrigger extends InvisibleJWindow {

	private final Log log = LogFactory.getLog(InvisibleJWindow.class);

	@Autowired
	private Director director;

	private Edge edge = Edge.LEFT;

	@ConfigProperty
	private Integer offset = 200;

	@ConfigProperty
	private Integer length = 500;

	@ConfigProperty
	private Integer thickness = 1;

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

		if ( edge == Edge.LEFT ) {
			setSize(thickness, length);
			setLocation(displayRect.x, displayRect.y + offset);
		} else {
			setSize(thickness, length);
			setLocation(displayRect.x + displayRect.width - getWidth(), displayRect.y + offset);
		}
	}

	public InvisibleJWindow setThickness(int thickness) {
		this.thickness = thickness;
		return this;
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
}
