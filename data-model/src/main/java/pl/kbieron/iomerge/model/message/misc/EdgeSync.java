package pl.kbieron.iomerge.model.message.misc;

import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


public class EdgeSync implements Message {

	private final static long serialVersionUID = 1L;

	private final Edge edge;

	public EdgeSync(Edge edge) {this.edge = edge;}

	@Override
	public void process(MessageProcessor processor) {
		processor.edgeSync(edge);
	}
}
