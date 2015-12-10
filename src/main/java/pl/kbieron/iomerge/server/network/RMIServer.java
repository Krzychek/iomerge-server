package pl.kbieron.iomerge.server.network;

import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import pl.kbieron.iomerge.iLipeRMI.IClient;
import pl.kbieron.iomerge.iLipeRMI.IServer;
import pl.kbieron.iomerge.model.ClientAction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.Socket;


@Service
public class RMIServer implements IServer, IClient, IServerListener {

	private final Log log = LogFactory.getLog(RMIServer.class);

	private Server server;

	private IClient client;

	private Socket socket;

	public RMIServer() {
		server = new Server();
		server.addServerListener(this);
	}

	@PostConstruct
	public void start() {
		CallHandler callHandler = new CallHandler();
		try {
			callHandler.registerGlobal(RMIServer.class, this);
			server.bind(7777, callHandler);
			log.info("Server Listening");
		} catch (LipeRMIException | IOException e) {
			log.error("failed to start server", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void clientConnected(Socket socket) {
		if ( !hasConnectedClient() ) {
			this.socket = socket;
			log.info("Client Connected: " + socket.getInetAddress());

		} else {
			try {
				log.warn("Already has connected client, closing incoming connection: " + socket);
				socket.close();
			} catch (IOException e) {
				log.warn("Already has connected client, closing incoming connection: ", e);
			}
		}
	}

	private boolean hasConnectedClient() {
		return socket != null && socket.isConnected() && client != null;
	}

	@Override
	public void clientDisconnected(Socket socket) {
		log.info("Client Disconnected: " + socket.getInetAddress());
		this.socket = null;
		this.client = null;
	}

	@Override
	public void setClient(IClient client, int width, int height) {
		this.client = client;
	}

	@Override
	public void action(ClientAction action) {
		client.action(action);

	}

	@Override
	public void moveMouse(int x, int y) {
		client.moveMouse(x, y);
	}

	@PreDestroy
	public void destroy() {
		server.close();
	}

}
