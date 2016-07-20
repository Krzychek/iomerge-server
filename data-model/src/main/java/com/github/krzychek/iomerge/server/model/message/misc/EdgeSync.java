package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.Edge;
import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.message.Message;


public class EdgeSync implements Message {

	private final static long serialVersionUID = 1L;

	private final Edge edge;

	public EdgeSync(Edge edge) {this.edge = edge;}

	@Override
	public void process(MessageProcessor processor) {
		processor.edgeSync(edge);
	}
}
