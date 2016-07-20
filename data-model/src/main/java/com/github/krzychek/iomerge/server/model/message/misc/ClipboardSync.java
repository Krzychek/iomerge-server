package com.github.krzychek.iomerge.server.model.message.misc;

import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.message.Message;


public class ClipboardSync implements Message {

	private final static long serialVersionUID = 1L;

	private final String text;

	public ClipboardSync(String text) {this.text = text;}

	@Override
	public void process(MessageProcessor processor) {
		processor.clipboardSync(text);

	}
}
