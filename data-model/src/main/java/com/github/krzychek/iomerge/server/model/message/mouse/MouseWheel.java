package com.github.krzychek.iomerge.server.model.message.mouse;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class MouseWheel implements Message {

	private final static long serialVersionUID = 1L;

	private final int wheelRotation;

	public MouseWheel(int wheelRotation) {this.wheelRotation = wheelRotation;}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseWheel(wheelRotation);
	}
}
