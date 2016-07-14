package pl.kbieron.iomerge.model.message.mouse;

import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;


public class MouseWheel implements Message {

	private final static long serialVersionUID = 1L;

	private final int wheelRotation;

	public MouseWheel(int wheelRotation) {this.wheelRotation = wheelRotation;}

	@Override
	public void process(MessageProcessor processor) {
		processor.mouseWheel(wheelRotation);
	}
}
