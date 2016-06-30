package pl.kbieron.iomerge.server.network;


import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.message.Message;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;

@Component
public class ConnectionHandlerProxy implements ConnectionHandler {


	private ConnectionHandler connectionHandler;

	@Override
	public void sendToClient(Message msg) {
		connectionHandler.sendToClient(msg);
	}

	@Override
	public void keepAlive() {
		connectionHandler.keepAlive();
	}

	@PostConstruct
	public void resetConnectionHandler() {
		this.connectionHandler = new NOOPConnectionHandler();
	}

	void connect(Socket clientSocket) throws IOException {
		SingleClientHandler clientHandler = SingleClientHandler.connect(clientSocket);
		this.connectionHandler = clientHandler;
		clientHandler.startReading();
	}

	private static class NOOPConnectionHandler implements ConnectionHandler {
		private final static String MSG = "Call to NOOPConnectionHandler";

		public void sendToClient(Message ignore) { Logger.debug(MSG); }

		public void keepAlive() { Logger.debug(MSG); }
	}
}
