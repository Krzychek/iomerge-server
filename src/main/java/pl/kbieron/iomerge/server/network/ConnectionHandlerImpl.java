package pl.kbieron.iomerge.server.network;

import org.apache.log4j.Logger;
import pl.kbieron.iomerge.model.MessageProcessor;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.misc.Heartbeat;
import pl.kbieron.iomerge.server.appState.AppStateManager;

import javax.inject.Inject;
import javax.swing.Timer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


class ConnectionHandlerImpl implements ConnectionHandler {

	private static final Logger log = Logger.getLogger(ConnectionHandlerImpl.class);

	private final Timer heartBeatTimer;

	private final Timer timeOutTimer;

	private final ObjectInputStream clientInputStream;

	private final Socket clientSocket;

	private final AppStateManager appStateManager;

	private final ObjectOutputStream clientOutputStream;

	@Inject
	private MsgProcessor msgProcessor;

	private ConnectionHandlerImpl(Socket clientSocket, AppStateManager appStateManager) throws IOException {
		this.appStateManager = appStateManager;

		this.clientSocket = clientSocket;
		this.clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
		this.clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

		Heartbeat heartbeat = new Heartbeat();
		heartBeatTimer = new Timer(4000, e -> sendToClient(heartbeat));
		heartBeatTimer.start();

		timeOutTimer = new Timer(5000, e -> disconnect());
		timeOutTimer.start();
	}

	static ConnectionHandlerImpl connect(Socket socket, int sendBufferSize, AppStateManager appStateManager)
			throws IOException {

		socket.setSendBufferSize(sendBufferSize);
		ConnectionHandlerImpl connectionHandler = new ConnectionHandlerImpl(socket, appStateManager);
		log.info("client connected");
		return connectionHandler;
	}

	void startReading() throws IOException {
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
		heartBeatTimer.stop();
		try {
			clientSocket.close();
		} catch (IOException e) {
			log.warn(e);
		}
		try {
			clientOutputStream.close();
		} catch (IOException e) {
			log.warn(e);
		}
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

	@Override
	public void keepAlive() {
		timeOutTimer.restart();
	}
}
