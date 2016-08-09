package com.github.krzychek.iomerge.server.model.message.mouse;

import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.message.Message;


public class MousePress implements Message {

	private final static long serialVersionUID = 1L;

	private final int button;

	public MousePress(int button) {
		this.button = button;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.mousePress(button);
	}
}
