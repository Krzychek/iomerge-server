package com.github.krzychek.iomerge.server.network;

import com.github.krzychek.iomerge.server.model.message.Message;


/**
 * Handles connection with client
 */
public interface ConnectionHandler {

	void sendToClient(Message msg);
}
