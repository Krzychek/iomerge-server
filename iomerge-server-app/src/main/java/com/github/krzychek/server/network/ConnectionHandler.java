package com.github.krzychek.server.network;

import com.github.krzychek.server.model.message.Message;


/**
 * Handles connection with client
 */
public interface ConnectionHandler {

	void sendToClient(Message msg);
}
