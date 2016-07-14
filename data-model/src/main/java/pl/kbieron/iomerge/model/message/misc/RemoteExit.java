package pl.kbieron.iomerge.model.message.misc;

import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


@SuppressWarnings("unused")
public class RemoteExit implements Message {

	private final static long serialVersionUID = 1L;

	@Override
	public void process(MessageProcessor processor) {
		processor.remoteExit();
	}
}
