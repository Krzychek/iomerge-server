package com.github.krzychek.server.network;

import com.github.krzychek.server.api.appState.AppState;
import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.model.message.Message;
import com.github.krzychek.server.model.message.misc.Heartbeat;
import com.github.krzychek.server.model.serialization.MessageIOFacade;
import org.pmw.tinylog.Logger;
import org.springframework.context.event.EventListener;

import javax.swing.Timer;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


class SingleClientHandler implements ConnectionHandler {

	private final Socket clientSocket;
	private final Timer heartBeatTimer;
	private final Timer timeOutTimer;
	private final MessageIOFacade messageIOFacade;
	private final AppStateManager appStateManager;
	private final MsgProcessor msgProcessor;
	private volatile boolean connected;

	SingleClientHandler(Socket clientSocket, MsgProcessor msgProcessor, AppStateManager appStateManager) throws IOException {
		this.msgProcessor = msgProcessor;
		this.appStateManager = appStateManager;
		this.connected = true;

		this.clientSocket = clientSocket;

		this.messageIOFacade = new MessageIOFacade(clientSocket);

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
			while (!messageIOFacade.isStopped())
				messageIOFacade.getMessage().process(msgProcessor);

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
			messageIOFacade.sendMessage(msg);
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
