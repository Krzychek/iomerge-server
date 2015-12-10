package pl.kbieron.iomerge.server.network;

import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import pl.kbieron.iomerge.model.RMIRemote;
import pl.kbieron.iomerge.model.RMIServerIface;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.Socket;


@Service
public class RMIServer implements RMIServerIface, RMIRemote, IServerListener {

	private final Log log = LogFactory.getLog(RMIServer.class);

	private Server server;

	private RMIRemote remote;

	private Socket socket;

	public RMIServer() {

		server = new Server();
		server.addServerListener(this);
	}

	@PostConstruct
	public void start() {
		CallHandler callHandler = new CallHandler();
		try {
			callHandler.registerGlobal(RMIServerIface.class, this);
			server.bind(7777, callHandler);
			log.info("Server Listening");
		} catch (LipeRMIException | IOException e) {
			log.error("failed to start server", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void hitBackBtn() {
		if ( hasConnectedClient() ) remote.hitBackBtn();
		else log.warn("hitBackBtn while not connected to client");
	}

	private boolean hasConnectedClient() {
		return socket != null && socket.isConnected() && remote != null;
	}

	@Override
	public void hitHomeBtn() {
		if ( hasConnectedClient() ) remote.hitHomeBtn();
		else log.warn("hiHomeBtn while not connected to client");
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

	@Override
	public void clientDisconnected(Socket socket) {
		log.info("Client Disconnected: " + socket.getInetAddress());
		this.socket = null;
		this.remote = null;
	}

	@Override
	public void setRemote(RMIRemote rmiRemote) {
		this.remote = rmiRemote;
	}

	@PreDestroy
	public void destroy() {
		server.close();
	}
}
