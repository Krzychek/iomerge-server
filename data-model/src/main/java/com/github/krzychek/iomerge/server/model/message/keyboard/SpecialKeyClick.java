package com.github.krzychek.iomerge.server.model.message.keyboard;

import com.github.krzychek.iomerge.server.model.SpecialKey;
import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class SpecialKeyClick implements Message {

	private final SpecialKey specialKey;

	public SpecialKeyClick(SpecialKey specialKey) {
		this.specialKey = specialKey;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.specialKeyClick(specialKey);

	}
}
