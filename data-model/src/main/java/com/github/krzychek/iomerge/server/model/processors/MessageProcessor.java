package com.github.krzychek.iomerge.server.model.processors;

@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public interface MessageProcessor extends
		KeyboardMessageProcessor,
		MouseMessageProcessor,
		MiscMessageProcessor,
		AndroidMessageProcessor {

}