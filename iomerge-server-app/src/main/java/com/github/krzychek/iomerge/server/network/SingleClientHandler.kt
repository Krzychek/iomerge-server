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
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


internal class SingleClientHandler(clientSocket: Socket, private val msgProcessor: MessageProcessor, private val appStateManager: AppStateManager)
: ConnectionHandler {

	private val HEARBEAT_DELAY = 2
	private val HEARTBEAT_TIMEOUT = HEARBEAT_DELAY * 2

	private var heartbeatExecutor = ScheduledThreadPoolExecutor(1)

	private val socket: MessageSocketWrapper = MessageSocketWrapper(clientSocket)

	@Volatile private var connected: Boolean = true

	@Volatile private var lastHeartbeatTime: Long = System.currentTimeMillis()

	private fun initTimers() {
		heartbeatExecutor.scheduleWithFixedDelay({
			sendToClient(Heartbeat.INSTANCE)
		}, 0, HEARBEAT_DELAY.toLong(), TimeUnit.SECONDS)


		lastHeartbeatTime = System.currentTimeMillis()
		var lastCall: Long = 0

		heartbeatExecutor.scheduleWithFixedDelay({
			if (lastCall > lastHeartbeatTime) {
				Logger.warn("Connection timeout")
				disconnect()
			}
			lastCall = System.currentTimeMillis()
		}, 2 * HEARTBEAT_TIMEOUT.toLong(), HEARTBEAT_TIMEOUT.toLong(), TimeUnit.SECONDS)
	}

	fun startReading() {
		Thread({

			appStateManager.connected()
			initTimers()

			socket.apply {
				while (isClosed.not()) {
					try {
						readMessage().process(msgProcessor)
						lastHeartbeatTime = System.currentTimeMillis()

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
		heartbeatExecutor.shutdown()

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
