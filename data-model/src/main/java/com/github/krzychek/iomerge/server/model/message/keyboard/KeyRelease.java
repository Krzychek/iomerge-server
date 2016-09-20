package com.github.krzychek.iomerge.server.model.message.keyboard;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class KeyRelease implements Message {

	private final static long serialVersionUID = 1L;

	private final int character;

	public KeyRelease(int character) {

		this.character = character;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.keyRelease(character);
	}
}
