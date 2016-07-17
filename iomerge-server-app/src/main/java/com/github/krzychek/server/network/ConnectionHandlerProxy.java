package com.github.krzychek.server.network;


import com.github.krzychek.server.model.message.Message;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;


@Component
public class ConnectionHandlerProxy implements ConnectionHandler {

	private final SingleClientHandlerFactory singleClientHandlerFactory;

	private ConnectionHandler connectionHandler;

	@Autowired
	public ConnectionHandlerProxy(SingleClientHandlerFactory singleClientHandlerFactory) {
		this.singleClientHandlerFactory = singleClientHandlerFactory;
	}

	@Override
	public void sendToClient(Message msg) {
		connectionHandler.sendToClient(msg);
	}

	@PostConstruct
	public void resetConnectionHandler() {
		this.connectionHandler = new NOOPConnectionHandler();
	}

	void connect(Socket clientSocket) throws IOException {
		SingleClientHandler clientHandler = singleClientHandlerFactory.createSingleClientHandler(clientSocket);
		this.connectionHandler = clientHandler;
		clientHandler.startReading();
	}

	private static class NOOPConnectionHandler implements ConnectionHandler {

		private final static String MSG = "Call to NOOPConnectionHandler";

		public void sendToClient(Message ignore) { Logger.debug(MSG); }
	}
}
