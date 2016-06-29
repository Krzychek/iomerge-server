package pl.kbieron.iomerge.server.network;

import pl.kbieron.iomerge.model.message.Message;


/**
 * Handles connection with client
 */
public interface ConnectionHandler {

	void sendToClient(Message msg);

	void keepAlive();
}
