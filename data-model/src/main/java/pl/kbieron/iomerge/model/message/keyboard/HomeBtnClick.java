package pl.kbieron.iomerge.model.message.keyboard;

import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


public class HomeBtnClick implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.homeBtnClick();
	}
}
