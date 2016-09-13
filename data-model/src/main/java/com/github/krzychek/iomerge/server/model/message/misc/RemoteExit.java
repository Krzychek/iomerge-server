package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


@SuppressWarnings("unused")
public class RemoteExit implements Message {

	private final static long serialVersionUID = 1L;

	private final float position;

	public RemoteExit(float position) {
		this.position = position;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.returnToLocal(position);
	}
}
