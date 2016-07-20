package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.message.Message;


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
