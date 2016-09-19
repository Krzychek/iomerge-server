package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.message.Message
import com.github.krzychek.iomerge.server.model.message.misc.Heartbeat
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor
import com.github.krzychek.iomerge.server.model.serialization.MessageSocketWrapper
import org.pmw.tinylog.Logger
import java.io.EOFException
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import javax.swing.Timer


internal class SingleClientHandler(clientSocket: Socket, private val msgProcessor: MessageProcessor, private val appStateManager: AppStateManager)
: ConnectionHandler {

	private val heartBeatTimer: Timer = Timer(2000, null)

	private val socket: MessageSocketWrapper = MessageSocketWrapper(clientSocket)

	@Volatile private var connected: Boolean = true

	private fun initTimers() {
		val heartbeat = Heartbeat()
		heartBeatTimer.addActionListener { e -> sendToClient(heartbeat) }
		heartBeatTimer.start()
	}

	fun startReading() {
		Thread({

			appStateManager.connected()
			initTimers()

			socket.apply {
				while (isClosed.not()) {
					try {
						readMessage().process(msgProcessor)

					} catch (e: EOFException) {
						disconnect()

					} catch (e: IOException) {
						disconnect()
						Logger.warn(e)

					} catch (e: Exception) {
						Logger.warn(e)
					}

				}
			}

		}, "Message Reading thread").start()
	}

	private fun disconnect() {
		if (!connected) return
		connected = false
		appStateManager.disconnected()

		Logger.info("disconnecting from client")
		heartBeatTimer.stop()

		try {
			socket.close()
		} catch (e: IOException) {
			Logger.warn(e)
		}

	}

	override fun sendToClient(msg: Message) {
		try {
			socket.sendMessage(msg)
		} catch (e: SocketException) {
			Logger.warn(e)
			disconnect()
		} catch (e: IOException) {
			Logger.error(e)
		}

	}
}
