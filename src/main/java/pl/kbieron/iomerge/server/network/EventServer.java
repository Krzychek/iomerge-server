package pl.kbieron.iomerge.server.network;

import org.annoprops.annotations.ConfigProperty;
import org.annoprops.annotations.PropertyHolder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.server.appState.AppStateManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/**
 * Main part of network module, starts up connection, passes over messages to concentrate connection handler
 */
@PropertyHolder
@Component
public class EventServer implements ConnectionHandler {

	private final Logger log = Logger.getLogger(EventServer.class);

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private MsgProcessor msgProcessor;

	private ConnectionHandler connectionHandler;

	private ServerSocket serverSocket;

	@ConfigProperty("ServerPort")
	private int port = 7698;

	@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
	@ConfigProperty("SendBufferSize")
	private int sendBufferSize = 512;

	@PreDestroy
	private void shutdown() {
		log.info("shutting down server");
		if (serverSocket != null) try {
			serverSocket.close();
		} catch (IOException e) {
			log.warn(e);
		}
		appStateManager.disconnected();
	}

	@PostConstruct
	public void start() throws IOException {
		log.info("starting server");
		serverSocket = new ServerSocket();
		serverSocket.setPerformancePreferences(1, 2, 0);
		serverSocket.bind(new InetSocketAddress(port));

		new Thread(this::acceptListener, "acceptListener at: " + port).start();
	}

	private void restart() {
		log.info("restarting server");
		shutdown();
		try {
			start();
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	private void acceptListener() {
		while (!serverSocket.isClosed()) {
			ConnectionHandlerImpl connectionHandler = null;
			try {
				Socket clientSocket = serverSocket.accept();
				log.info("socket client accepted");
				connectionHandler = ConnectionHandlerImpl.connect(clientSocket, sendBufferSize, appStateManager, msgProcessor);
				this.connectionHandler = connectionHandler;
				appStateManager.connected();
				connectionHandler.startReading();
			} catch (SocketException e) {
				log.debug(e);
			} catch (IOException e) {
				log.warn("Problem with connection", e);
			} finally {
				if (connectionHandler != null) connectionHandler.disconnect();
			}
		}
	}

	public void sendToClient(Message msg) {
		if (connectionHandler != null)
			connectionHandler.sendToClient(msg);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if (this.port == port) return;
		this.port = port;
		if (serverSocket != null && serverSocket.isBound()) restart();
	}
}
