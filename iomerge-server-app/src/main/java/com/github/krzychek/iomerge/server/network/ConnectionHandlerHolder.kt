package com.github.krzychek.iomerge.server.network


import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.message.Message
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor
import org.pmw.tinylog.Logger
import java.net.Socket
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class ConnectionHandlerHolder
@Inject constructor(private val messageProcessor: MessageProcessor,
					private val appStateManager: AppStateManager,
					private val scheduledExecutorService: ScheduledExecutorService) : ConnectionHandler {

	private var connectionHandler: ConnectionHandler? = null

	override fun sendToClient(msg: Message)
			= connectionHandler?.sendToClient(msg) ?: Logger.warn("Calling sendToClient without connected client")

	fun connect(clientSocket: Socket) =
			SingleClientHandler(clientSocket, messageProcessor, appStateManager, scheduledExecutorService).let {
				connectionHandler = it
				it.startReading()
			}
}
