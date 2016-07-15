package com.github.krzychek.server.model.message;

import com.github.krzychek.server.model.MessageProcessor;

import java.io.Serializable;


public interface Message extends Serializable {

	void process(MessageProcessor processor);
}
