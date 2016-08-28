package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.google.common.eventbus.Subscribe
import org.annoprops.annotations.ConfigProperty
import org.annoprops.annotations.PropertyHolder
import org.pmw.tinylog.Logger
import org.springframework.stereotype.Component
import java.io.EOFException
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.Executors
import javax.annotation.PreDestroy


/**
 * Waits for clients and starts up connection
 */
@PropertyHolder
@Component
open class EventServer(private val appStateManager: AppStateManager, private val connectionHandlerProxy: ConnectionHandlerProxy) {
	private val executor = Executors.newSingleThreadExecutor()

	private var serverSocket: ServerSocket? = null

	@ConfigProperty("ServerPort")
	var port = 7698
		set(port) {
			if (field != port) {
				field = port
				if (serverSocket != null) restart()
			}

		}

	@PreDestroy
	private fun shutdown() {
		Logger.info("shutting down server")

		try {
			serverSocket?.close()
			serverSocket = null
		} catch (e: IOException) {
			Logger.warn(e)
		}

		appStateManager.disconnected()
	}

	private fun restart() {
		Logger.info("restarting server")
		shutdown()
		acceptListener()
	}

	private fun acceptListener() = executor.execute {
		val serverSocket = ServerSocket().apply {
			setPerformancePreferences(1, 2, 0)
			bind(InetSocketAddress(port))
		}
		this.serverSocket = serverSocket
		Logger.info("listening at port " + this.port)

		try {
			val clientSocket = serverSocket.accept()
			Logger.info("client socket accepted")
			connectionHandlerProxy.connect(clientSocket)

		} catch (e: EOFException) {
			Logger.info("Problem with connection", e)
		} catch (e: IOException) {
			Logger.warn("Problem with connection", e)
		}
	}


	@Subscribe
	fun onAppStateChange(appState: AppState) {
		if (AppState.DISCONNECTED == appState) {
			this.acceptListener()
		}
	}
}
