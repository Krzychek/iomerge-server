package com.github.krzychek.server.model.message.misc;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


@SuppressWarnings("unused")
public class RemoteExit implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.remoteExit();
	}
}
