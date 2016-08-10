package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import org.annoprops.annotations.ConfigProperty
import org.annoprops.annotations.PropertyHolder
import org.pmw.tinylog.Logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


/**
 * Waits for clients and starts up connection
 */
@PropertyHolder
@Component
open class EventServer(private val appStateManager: AppStateManager, private val connectionHandlerProxy: ConnectionHandlerProxy) {
	private val executor = Executors.newSingleThreadExecutor()

	private lateinit var serverSocket: ServerSocket

	@ConfigProperty("ServerPort")
	var port = 7698
		set(port) {
			if (field != port) {
				field = port
				if (serverSocket.isBound)
					restart()
			}

		}

	@PreDestroy
	private fun shutdown() {
		Logger.info("shutting down server")

		try {
			serverSocket.close()
		} catch (e: IOException) {
			Logger.warn(e)
		}

		appStateManager.disconnected()
	}

	@PostConstruct
	fun start() {
		Logger.info("starting server at port " + this.port)
		serverSocket = ServerSocket().apply {
			setPerformancePreferences(1, 2, 0)
			bind(InetSocketAddress(port))
		}
		Logger.info("listening at port " + this.port)
	}

	private fun restart() {
		Logger.info("restarting server")
		shutdown()
		try {
			start()
		} catch (e: IOException) {
			Logger.error(e)
			throw RuntimeException(e)
		}

	}

	private fun acceptListener() {
		try {
			val clientSocket = serverSocket.accept()
			Logger.info("client socket accepted")
			connectionHandlerProxy.connect(clientSocket)

		} catch (e: IOException) {
			Logger.warn("Problem with connection", e)
		}

	}

	@EventListener
	private fun onAppStateChange(appState: AppState) {
		if (AppState.DISCONNECTED == appState) {
			executor.execute { this.acceptListener() }
		}
	}
}
