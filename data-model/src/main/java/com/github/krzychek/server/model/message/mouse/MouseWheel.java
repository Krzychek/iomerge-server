package com.github.krzychek.server.model.message.mouse;

import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.message.Message;


public class MouseWheel implements Message {

	private final static long serialVersionUID = 1L;

	private final int wheelRotation;

	public MouseWheel(int wheelRotation) {this.wheelRotation = wheelRotation;}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseWheel(wheelRotation);
	}
}
