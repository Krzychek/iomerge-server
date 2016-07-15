package com.github.krzychek.server.model.message.mouse;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


public class MousePress implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.mousePress();
	}
}
