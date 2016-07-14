package pl.kbieron.iomerge.model.message;

import pl.kbieron.iomerge.model.MessageProcessor;

import java.io.Serializable;


public interface Message extends Serializable {

	void process(MessageProcessor processor);
}
