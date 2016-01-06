package pl.kbieron.iomerge.server.network;

import org.apache.log4j.Logger;
import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.misc.Heartbeat;
import pl.kbieron.iomerge.server.appState.AppStateManager;

import javax.swing.Timer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


class ConnectionHandlerImpl implements ConnectionHandler {

	private static final Logger log = Logger.getLogger(ConnectionHandlerImpl.class);

	private final Timer heartBeetTimer;

	private final ObjectInputStream clientInputStream;

	private final Socket clientSocket;

	private final AppStateManager appStateManager;

	private final ObjectOutputStream clientOutputStream;

	private ConnectionHandlerImpl(Socket clientSocket, AppStateManager appStateManager) throws IOException {
		this.appStateManager = appStateManager;

		this.clientSocket = clientSocket;
		this.clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
		this.clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

		heartBeetTimer = new Timer(2000, e -> sendToClient(new Heartbeat()));
		heartBeetTimer.start();
	}

	static ConnectionHandlerImpl connect(Socket socket, int sendBufferSize, AppStateManager appStateManager)
			throws IOException {

		socket.setSendBufferSize(sendBufferSize);
		ConnectionHandlerImpl connectionHandler = new ConnectionHandlerImpl(socket, appStateManager);
		log.info("client connected");
		appStateManager.connected();
		return connectionHandler;
	}

	void startReading(MessageProcessor msgProcessor) throws IOException {
		//noinspection InfiniteLoopStatement
		while ( true ) {
			try {
				((Message) clientInputStream.readObject()).process(msgProcessor);
			} catch (ClassNotFoundException e) {
				log.warn(e);
			}
		}
	}

	void disconnect() {
		log.info("disconnecting from client");
		heartBeetTimer.stop();
		//		try {
		//			clientSocket.close();
		//		} catch (IOException e) {
		//			log.warn(e);
		//		}
		//		try {
		//			clientOutputStream.close();
		//		} catch (IOException e) {
		//			log.warn(e);
		//		}
		appStateManager.disconnected();
	}

	@Override
	public void sendToClient(Message msg) {
		try {
			clientOutputStream.writeObject(msg);
		} catch (SocketException e) {
			log.debug(e);
			disconnect();
		} catch (IOException e) {
			log.error(e);
		}
	}
}