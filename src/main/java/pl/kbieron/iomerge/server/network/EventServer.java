package pl.kbieron.iomerge.server.network;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.misc.Heartbeat;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

import javax.annotation.PreDestroy;
import javax.swing.Timer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


@Component
public class EventServer {

	private final Logger log = Logger.getLogger(EventServer.class);

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private RemoteMsgProcessorAdapter msgProcessor;

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private ObjectOutputStream clientOutputStream;

	@ConfigProperty( "ServerPort" )
	private int port = 7698;

	private Timer heartBeetTimer;

	@SuppressWarnings( {"FieldCanBeLocal", "CanBeFinal"} )
	@ConfigProperty( "SendBufferSize" )
	private int sendBufferSize = 512;

	private void disconnectClient() {
		log.info("disconnecting from client");
		heartBeetTimer.stop();
		try {
			if ( clientSocket != null ) clientSocket.close();
		} catch (IOException e) {
			log.warn(e);
		}
		try {
			if ( clientOutputStream != null ) clientOutputStream.close();
		} catch (IOException e) {
			log.warn(e);
		}
		clientSocket = null;
		clientOutputStream = null;
		appStateManager.disconnected();
	}

	@PreDestroy
	private void shutdown() {
		log.info("shutting down server");
		if ( serverSocket != null ) try {
			serverSocket.close();
		} catch (IOException e) {
			log.warn(e);
		}
		if ( heartBeetTimer != null ) heartBeetTimer.stop();
		appStateManager.disconnected();
	}

	public void start() throws IOException {
		log.info("starting server");
		serverSocket = new ServerSocket();
		serverSocket.setPerformancePreferences(1, 2, 0);
		serverSocket.bind(new InetSocketAddress(port));

		heartBeetTimer = new Timer(2000, e -> sendToClient(new Heartbeat()));
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
		while ( !serverSocket.isClosed() ) {
			try {
				Socket clientSocket = serverSocket.accept();
				log.info("socket client accepted");
				setupClientSocket(clientSocket);
				log.info("client connected");
				startReading();
			} catch (SocketException ignored) {
			} catch (IOException e) {
				log.warn("Problem with connection", e);
			} finally {
				disconnectClient();
			}
		}
	}

	private void setupClientSocket(Socket newClient) throws IOException {
		if ( clientSocket != null ) {
			clientSocket.close();
		}
		newClient.setSendBufferSize(sendBufferSize);
		clientSocket = newClient;
		clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		heartBeetTimer.start();
		appStateManager.connected();
	}

	private void startReading() throws IOException {
		ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		//noinspection InfiniteLoopStatement
		while ( true ) {
			try {
				((Message) objectInputStream.readObject()).process(msgProcessor);
			} catch (ClassNotFoundException e) {
				log.warn(e);
			}
		}
	}

	void sendToClient(Message msg) {
		try {
			if ( clientOutputStream != null ) {
				clientOutputStream.writeObject(msg);
			}
		} catch (SocketException e) {
			log.debug(e);
			disconnectClient();
		} catch (IOException e) {
			log.error(e);
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if ( this.port == port ) return;
		this.port = port;
		if ( serverSocket != null && serverSocket.isBound() ) restart();
	}
}
