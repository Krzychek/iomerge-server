package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.Edge;
import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class EdgeSync implements Message {

	private final static long serialVersionUID = 1L;

	private final Edge edge;

	public EdgeSync(Edge edge) {this.edge = edge;}

	@Override
	public void process(MessageProcessor processor) {
		processor.edgeSync(edge);
	}
}
