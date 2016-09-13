package com.github.krzychek.iomerge.server.model.message.keyboard;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class HomeBtnClick implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.homeBtnClick();
	}
}
