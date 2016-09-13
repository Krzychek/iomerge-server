package com.github.krzychek.iomerge.server.model.message;

import com.github.krzychek.iomerge.server.model.processors.MessageProcessor;

import java.io.Serializable;


public interface Message extends Serializable {

	void process(MessageProcessor processor);
}
