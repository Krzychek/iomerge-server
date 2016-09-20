package com.github.krzychek.iomerge.server.model.message.keyboard;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class StringTyped implements Message {

	private final String string;

	public StringTyped(String string) {
		this.string = string;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.stringTyped(string);
	}
}
