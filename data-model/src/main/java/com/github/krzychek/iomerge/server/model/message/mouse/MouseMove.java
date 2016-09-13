package com.github.krzychek.iomerge.server.model.message.mouse;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class MouseMove implements Message {

	private final static long serialVersionUID = 1L;

	private final int dx;

	private final int dy;

	public MouseMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseMove(dx, dy);
	}
}
