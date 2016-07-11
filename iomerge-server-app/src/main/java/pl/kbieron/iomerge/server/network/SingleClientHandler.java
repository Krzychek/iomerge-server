package pl.kbieron.iomerge.server.network;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.event.EventListener;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.misc.Heartbeat;
import pl.kbieron.iomerge.server.api.appState.AppState;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
class SingleClientHandler implements ConnectionHandler {


	private static final int SEND_BUFFER_SIZE = 512;

	private final Socket clientSocket;
	private final Timer heartBeatTimer;
	private final Timer timeOutTimer;
	private final ObjectOutputStream clientOutputStream;

	private volatile boolean connected;

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private MsgProcessor msgProcessor;


	private SingleClientHandler(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		clientSocket.setSendBufferSize(SEND_BUFFER_SIZE);
		clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

		Heartbeat heartbeat = new Heartbeat();
		this.heartBeatTimer = new Timer(2000, e -> sendToClient(heartbeat));
		this.heartBeatTimer.start();

		this.timeOutTimer = new Timer(5000, e -> {}); // TODOe -> disconnect());
		this.timeOutTimer.start();
		this.connected = true;
	}

	static SingleClientHandler connect(Socket clientSocket) throws IOException {
		return new SingleClientHandler(clientSocket);
	}

	void startReading() throws IOException {
		appStateManager.connected();

		ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
		try {
			while (connected) {
				((Message) clientInputStream.readObject()).process(msgProcessor);
			}

		} catch (ClassNotFoundException e) { Logger.warn(e); }
	}

	private void disconnect() {
		if (!connected) return;
		connected = false;
		appStateManager.disconnected();

		Logger.info("disconnecting from client");
		heartBeatTimer.stop();
		timeOutTimer.stop();

		try {
			clientSocket.close();
		} catch (IOException e) { Logger.warn(e); }

		try {
			clientOutputStream.close();
		} catch (IOException e) { Logger.warn(e); }
	}

	@EventListener
	private void onAppStateChange(AppState appState) {
		if (appState.equals(AppState.DISCONNECTED) && connected) {
			disconnect();
		}
	}

	@Override
	public void sendToClient(Message msg) {
		try {
			clientOutputStream.writeObject(msg);
		} catch (SocketException e) {
			Logger.warn(e);
			disconnect();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public void keepAlive() {
		timeOutTimer.restart();
	}
}
