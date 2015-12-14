package pl.kbieron.iomerge.server.network;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.swing.Timer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


@Component
public class EventServer {

	private final Log log = LogFactory.getLog(EventServer.class);

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private ObjectOutputStream clientOutputStream;

	@ConfigProperty
	private int port = 7698;

	public void close() {
		disconnectClient();
		try {
			serverSocket.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

	private void disconnectClient() {
		try {
			clientSocket.close();
			clientSocket = null;
		} catch (IOException e) {
			log.error(e);
		}
	}

	@PostConstruct
	public void bind() throws IOException {
		serverSocket = new ServerSocket();
		serverSocket.setPerformancePreferences(1, 2, 0);
		serverSocket.bind(new InetSocketAddress(port));

		new Thread(this::acceptListener, String.format("acceptListener at :%d", port)) //
				.start();
	}

	private void acceptListener() {
		while ( serverSocket.isBound() ) {
			try {
				Socket newClient = serverSocket.accept();
				sendHeartBeat(null);
				if ( clientSocket == null ) {
					clientSocket = newClient;
					clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					new Timer(500, this::sendHeartBeat);
					log.info("client connected");
				} else {
					newClient.close();
					log.warn("another client, closing connection");
				}
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	@SuppressWarnings( "UnusedParameters" )
	public void sendHeartBeat(Object ignored) {
		sendToClient((byte) 0xff);
	}

	public void sendToClient(byte... bytes) {
		try {
			if ( clientSocket != null ) {
				clientOutputStream.writeObject(bytes);
			}
		} catch (SocketException e) {
			disconnectClient();
		} catch (IOException e) {
			log.error(e);
		}
	}

}
