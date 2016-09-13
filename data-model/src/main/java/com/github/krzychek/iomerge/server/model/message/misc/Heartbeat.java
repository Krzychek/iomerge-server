package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class Heartbeat implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.heartbeat();
	}
}
