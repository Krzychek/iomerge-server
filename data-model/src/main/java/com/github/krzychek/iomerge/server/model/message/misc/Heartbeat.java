package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class Heartbeat implements Message {

	public static final Heartbeat INSTANCE = new Heartbeat();

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {}
}
