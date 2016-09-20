package com.github.krzychek.iomerge.server.model.processors;

import com.github.krzychek.iomerge.server.model.Edge;


public interface MiscMessageProcessor {

	void edgeSync(Edge edge);

	void returnToLocal(float position);

	void clipboardSync(String text);
}
