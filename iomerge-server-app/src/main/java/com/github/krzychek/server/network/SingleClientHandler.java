package com.github.krzychek.server.network;

import com.github.krzychek.server.api.appState.AppState;
import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.model.message.Message;
import com.github.krzychek.server.model.message.misc.Heartbeat;
import com.github.krzychek.server.model.serialization.MessageSocketWrapper;
import org.pmw.tinylog.Logger;
import org.springframework.context.event.EventListener;

import javax.swing.Timer;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


class SingleClientHandler implements ConnectionHandler {

	private final Timer heartBeatTimer;
	private final MessageSocketWrapper socket;
	private final AppStateManager appStateManager;
	private final MsgProcessor msgProcessor;
	private volatile boolean connected;

	SingleClientHandler(Socket clientSocket, MsgProcessor msgProcessor, AppStateManager appStateManager) throws IOException {
		this.msgProcessor = msgProcessor;
		this.appStateManager = appStateManager;
		this.connected = true;

		this.socket = new MessageSocketWrapper(clientSocket);

		this.heartBeatTimer = new Timer(2000, null);

	}

	private void initTimers() {
		Heartbeat heartbeat = new Heartbeat();
		heartBeatTimer.addActionListener(e -> sendToClient(heartbeat));
		heartBeatTimer.start();
	}

	void startReading() {
		new Thread(() -> {

			appStateManager.connected();
			initTimers();

			while (!socket.isClosed()) {
				try {
					socket.getMessage().process(msgProcessor);

				} catch (EOFException e) {
					disconnect();

				} catch (IOException e) {
					disconnect();
					Logger.warn(e);

				} catch (ClassNotFoundException e) {
					Logger.warn(e);
				}
			}

		}, "Message Reading thread").start();
	}

	private void disconnect() {
		if (!connected) return;
		connected = false;
		appStateManager.disconnected();

		Logger.info("disconnecting from client");
		heartBeatTimer.stop();

		try {
			socket.close();
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
			socket.sendMessage(msg);
		} catch (SocketException e) {
			Logger.warn(e);
			disconnect();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
