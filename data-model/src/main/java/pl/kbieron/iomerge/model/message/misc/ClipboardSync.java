package pl.kbieron.iomerge.model.message.misc;

import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


public class ClipboardSync implements Message {

	private final static long serialVersionUID = 1L;

	private final String text;

	public ClipboardSync(String text) {this.text = text;}

	@Override
	public void process(MessageProcessor processor) {
		processor.clipboardSync(text);

	}
}
