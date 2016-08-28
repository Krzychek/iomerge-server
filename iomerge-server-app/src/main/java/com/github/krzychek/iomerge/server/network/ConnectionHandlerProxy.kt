package com.github.krzychek.iomerge.server.network


import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.model.message.Message
import org.springframework.stereotype.Component
import java.net.Socket


@Component
open class ConnectionHandlerProxy(private val messageProcessor: MessageProcessor, private val appStateManager: AppStateManager) : ConnectionHandler {

	private var connectionHandler: ConnectionHandler? = null

	override fun sendToClient(msg: Message) = connectionHandler!!.sendToClient(msg)

	fun connect(clientSocket: Socket) {
		SingleClientHandler(clientSocket, messageProcessor, appStateManager).let {
			connectionHandler = it
			it.startReading()
		}
	}
}
