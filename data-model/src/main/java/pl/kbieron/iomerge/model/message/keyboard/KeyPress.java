package pl.kbieron.iomerge.model.message.keyboard;

import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


public class KeyPress implements Message {

	private final static long serialVersionUID = 1L;

	private final int keyCode;

	public KeyPress(int keyCode) {

		this.keyCode = keyCode;
	}

	@Override
	public void process(MessageProcessor processor) {
		processor.keyPress(keyCode);
	}
}
