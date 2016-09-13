package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.message.Message;
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;


public class ClipboardSync implements Message {

	private final static long serialVersionUID = 1L;

	private final String text;

	public ClipboardSync(String text) {this.text = text;}

	@Override
	public void process(MessageProcessor processor) {
		processor.clipboardSync(text);

	}
}
