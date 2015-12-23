package pl.kbieron.iomerge.server.network;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

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
	private RemoteMsgProcessor msgProcessor;

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private ObjectOutputStream clientOutputStream;

	@ConfigProperty( "ServerPort" )
	private Integer port = 7698;

	private Timer heartBeetTimer;

	private void disconnectClient() {
		heartBeetTimer.stop();
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
		clientSocket = null;
		clientOutputStream = null;
		appStateManager.disconnected();
	}

	public void start() throws IOException {
		serverSocket = new ServerSocket();
		serverSocket.setPerformancePreferences(1, 2, 0);
		serverSocket.bind(new InetSocketAddress(port));

		heartBeetTimer = new Timer(500, e -> sendToClient((byte) 0xff));
		new Thread(this::acceptListener, String.format("acceptListener at :%d", port)) //
				.start();
	}

	private void acceptListener() {
		while ( serverSocket.isBound() ) {
			try {
				Socket newClient = serverSocket.accept();
				if ( clientSocket == null ) {
					setupClientSocket(newClient);
					log.info("client connected");
					startReading();
				} else {
					newClient.close();
					log.warn("another client, closing connection");
				}
			} catch (SocketException e) {
				log.debug(e);
				disconnectClient();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	private void setupClientSocket(Socket newClient) throws IOException {
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
				msgProcessor.process((byte[]) objectInputStream.readObject());
			} catch (ClassNotFoundException e) {
				log.warn(e);
			}
		}
	}

	void sendToClient(byte... bytes) {
		try {
			if ( clientOutputStream != null ) {
				clientOutputStream.writeObject(bytes);
			}
		} catch (SocketException e) {
			log.debug(e);
			disconnectClient();
		} catch (IOException e) {
			log.error(e);
		}
	}

}
