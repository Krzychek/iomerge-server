package com.github.krzychek.server.model.message.mouse;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


public class MouseSync implements Message {

	private final static long serialVersionUID = 1L;

	private final int x;

	private final int y;

	public MouseSync(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseSync(x, y);
	}
}
