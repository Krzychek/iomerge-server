package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.google.common.eventbus.Subscribe
import org.annoprops.annotations.ConfigProperty
import org.pmw.tinylog.Logger
import java.io.EOFException
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Waits for clients and starts up connection
 */
@Singleton class ServerManager
@Inject constructor(private val appStateManager: AppStateManager, private val connectionHandlerProxy: ConnectionHandlerHolder) {
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

	private fun shutdown() {
		Logger.info("shutting down server")

		try {
			serverSocket?.close()
			serverSocket = null
		} catch (e: IOException) {
			Logger.warn(e)
		}
	}

	private fun restart() {
		Logger.info("restarting server")
		shutdown()
		acceptListener()
	}

	private fun acceptListener() = executor.execute {
		serverSocket?.close()
		ServerSocket().apply {
			setPerformancePreferences(1, 2, 0)
			bind(InetSocketAddress(port))
			serverSocket = this
			Logger.info("listening at port: $port")

		}.apply {

			try {
				val clientSocket = accept()
				connectionHandlerProxy.connect(clientSocket)
				Logger.info("client socket accepted")

			} catch (e: EOFException) {
				Logger.info("Problem with connection", e)
			} catch (e: IOException) {
				Logger.warn("Problem with connection", e)
			}
		}.close()
	}


	@Subscribe
	fun onAppStateChange(appState: AppState) {
		if (appState == AppState.DISCONNECTED) acceptListener()
		else if (appState == AppState.SHUTDOWN) shutdown()
	}
}
