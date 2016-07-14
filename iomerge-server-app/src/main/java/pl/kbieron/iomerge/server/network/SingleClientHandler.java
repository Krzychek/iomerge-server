package pl.kbieron.iomerge.server.network;

import org.pmw.tinylog.Logger;
import org.springframework.context.event.EventListener;
import pl.kbieron.iomerge.model.message.Message;
import pl.kbieron.iomerge.model.message.misc.Heartbeat;
import pl.kbieron.iomerge.server.api.appState.AppState;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;

import javax.swing.Timer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


class SingleClientHandler implements ConnectionHandler {

	private static final int SEND_BUFFER_SIZE = 1;

	private final Socket clientSocket;
	private final Timer heartBeatTimer;
	private final Timer timeOutTimer;
	private final ObjectOutputStream clientOutputStream;
	private final ObjectInputStream clientInputStream;
	private final AppStateManager appStateManager;
	private final MsgProcessor msgProcessor;

	private volatile boolean connected;

	SingleClientHandler(Socket clientSocket, MsgProcessor msgProcessor, AppStateManager appStateManager) throws IOException {
		this.msgProcessor = msgProcessor;
		this.appStateManager = appStateManager;
		this.connected = true;

		this.clientSocket = clientSocket;
		clientSocket.setSendBufferSize(SEND_BUFFER_SIZE);

		this.clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		this.clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

		this.heartBeatTimer = new Timer(2000, null);

		this.timeOutTimer = new Timer(5000, null);

	}

	private void initTimers() {
		Heartbeat heartbeat = new Heartbeat();
		heartBeatTimer.addActionListener(e -> sendToClient(heartbeat));
		heartBeatTimer.start();

		timeOutTimer.addActionListener(e -> {}); // TODOe -> disconnect());
		timeOutTimer.start();
	}

	void startReading() throws IOException {
		appStateManager.connected();
		initTimers();


		try {
			while (connected) {
				((Message) clientInputStream.readObject()).process(msgProcessor);
			}

		} catch (ClassNotFoundException e) {
			Logger.warn(e);
		}
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
		} catch (IOException e) {
			Logger.warn(e);
		}

		try {
			clientOutputStream.close();
		} catch (IOException e) {
			Logger.warn(e);
		}
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
