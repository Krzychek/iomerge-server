package com.github.krzychek.iomerge.server.network


import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.model.message.Message
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class ConnectionHandlerProxy
@Inject constructor(private val messageProcessor: MessageProcessor, private val appStateManager: AppStateManager) : ConnectionHandler {

	private var connectionHandler: ConnectionHandler? = null

	override fun sendToClient(msg: Message) = connectionHandler!!.sendToClient(msg)

	fun connect(clientSocket: Socket) {
		SingleClientHandler(clientSocket, messageProcessor, appStateManager).let {
			connectionHandler = it
			it.startReading()
		}
	}
}
