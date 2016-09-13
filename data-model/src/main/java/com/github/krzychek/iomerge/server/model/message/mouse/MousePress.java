package com.github.krzychek.iomerge.server.model.message.mouse;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class MousePress implements Message {

	private final static long serialVersionUID = 1L;

	private final MouseButton button;

	public MousePress(MouseButton button) {
		this.button = button;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.mousePress(button);
	}
}
