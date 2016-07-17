package com.github.krzychek.server.model.message.mouse;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


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
