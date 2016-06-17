package pl.kbieron.iomerge.server.network;

import pl.kbieron.iomerge.model.message.Message;


/**
 * Handles connection with client
 */
interface ConnectionHandler {

	void sendToClient(Message msg);

	default void keepAlive() {}
}
