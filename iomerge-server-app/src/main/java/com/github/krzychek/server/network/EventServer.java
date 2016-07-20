package com.github.krzychek.server.network;

import com.github.krzychek.server.api.appState.AppState;
import com.github.krzychek.server.api.appState.AppStateManager;
import org.annoprops.annotations.ConfigProperty;
import org.annoprops.annotations.PropertyHolder;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Waits for clients and starts up connection
 */
@PropertyHolder
@Component
public class EventServer {

	private final AppStateManager appStateManager;
	private final ConnectionHandlerProxy connectionHandlerProxy;
	private final Executor executor;

	private ServerSocket serverSocket;

	@ConfigProperty("ServerPort")
	private int port = 7698;

	@Autowired
	public EventServer(AppStateManager appStateManager, ConnectionHandlerProxy connectionHandlerProxy) {
		this.appStateManager = appStateManager;
		this.connectionHandlerProxy = connectionHandlerProxy;
		this.executor = Executors.newSingleThreadExecutor();
	}

	@PreDestroy
	private void shutdown() {
		Logger.info("shutting down server");

		if (serverSocket != null) try {
			serverSocket.close();
		} catch (IOException e) {
			Logger.warn(e);
		}

		appStateManager.disconnected();
	}

	@PostConstruct
	public void start() throws IOException {
		Logger.info("starting server at port " + port);
		serverSocket = new ServerSocket();
		serverSocket.setPerformancePreferences(1, 2, 0);
		serverSocket.bind(new InetSocketAddress(port));
		Logger.info("listening at port " + port);
	}

	private void restart() {
		Logger.info("restarting server");
		shutdown();
		try {
			start();
		} catch (IOException e) {
			Logger.error(e);
			throw new RuntimeException(e);
		}
	}

	private void acceptListener() {
		try {
			Socket clientSocket = serverSocket.accept();
			Logger.info("client socket accepted");
			connectionHandlerProxy.connect(clientSocket);

		} catch (IOException e) {
			Logger.warn("Problem with connection", e);
		}
	}

	@EventListener
	private void onAppStateChange(AppState appState) {
		if (AppState.DISCONNECTED.equals(appState)) {
			executor.execute(this::acceptListener);
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if (this.port == port) return;
		this.port = port;
		if (serverSocket != null && serverSocket.isBound()) restart();
	}
}
