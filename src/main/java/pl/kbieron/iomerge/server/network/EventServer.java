package pl.kbieron.iomerge.server.network;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.properties.ConfigProperty;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


@Component
public class EventServer {

	private final Log log = LogFactory.getLog(EventServer.class);

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private ObjectOutputStream clientOutputStream;

	@ConfigProperty
	private int port = 7698;

	public void close() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			log.error(e);
		}
		try {
			serverSocket.close();
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
				if ( clientSocket == null || !clientSocket.isConnected() ) {
					clientSocket = newClient;
					clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
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

	public void sendToClient(byte[] bytes) {
		try {
			clientOutputStream.writeObject(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
