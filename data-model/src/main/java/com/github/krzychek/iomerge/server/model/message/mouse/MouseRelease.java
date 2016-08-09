package com.github.krzychek.iomerge.server.model.message.mouse;

import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.message.Message;


public class MouseRelease implements Message {

	private final static long serialVersionUID = 1L;

	private final int button;

	public MouseRelease(int button) {
		this.button = button;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseRelease(button);
	}
}
