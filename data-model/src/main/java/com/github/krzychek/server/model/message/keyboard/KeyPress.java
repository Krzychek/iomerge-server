package com.github.krzychek.server.model.message.keyboard;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


public class KeyPress implements Message {

	private final static long serialVersionUID = 1L;

	private final int keyCode;

	public KeyPress(int keyCode) {

		this.keyCode = keyCode;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.keyPress(keyCode);
	}
}
