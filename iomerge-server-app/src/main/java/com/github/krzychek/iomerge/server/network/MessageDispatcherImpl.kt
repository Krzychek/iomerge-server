package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.Order
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.model.message.Message
import com.github.krzychek.iomerge.server.model.message.keyboard.KeyClick
import com.github.krzychek.iomerge.server.model.message.keyboard.KeyPress
import com.github.krzychek.iomerge.server.model.message.keyboard.KeyRelease
import com.github.krzychek.iomerge.server.model.message.misc.ClipboardSync
import com.github.krzychek.iomerge.server.model.message.misc.EdgeSync
import com.github.krzychek.iomerge.server.model.message.mouse.MouseMove
import com.github.krzychek.iomerge.server.model.message.mouse.MousePress
import com.github.krzychek.iomerge.server.model.message.mouse.MouseRelease
import com.github.krzychek.iomerge.server.model.message.mouse.MouseWheel
import javax.inject.Inject
import javax.inject.Singleton


@Order(0)
@Singleton class MessageDispatcherImpl
@Inject constructor(private val connectionHandler: ConnectionHandlerHolder) : MessageDispatcher {

	private lateinit var nextInChain: MessageDispatcher

	override fun dispatchMouseMove(x: Int, y: Int) {
		connectionHandler.sendToClient(MouseMove(x, y))
		nextInChain.dispatchMouseMove(x, y)
	}

	override fun dispatchMousePress(button: Int) {
		connectionHandler.sendToClient(MousePress(button))
		nextInChain.dispatchMousePress(button)
	}

	override fun dispatchMouseRelease(button: Int) {
		connectionHandler.sendToClient(MouseRelease(button))
		nextInChain.dispatchMouseRelease(button)
	}

	override fun dispatchKeyPress(keyCode: Int) {
		connectionHandler.sendToClient(KeyPress(keyCode))
		nextInChain.dispatchKeyPress(keyCode)
	}

	override fun dispatchKeyRelease(keyCode: Int) {
		connectionHandler.sendToClient(KeyRelease(keyCode))
		nextInChain.dispatchKeyRelease(keyCode)
	}

	override fun dispatchMouseWheelEvent(wheelRotation: Int) {
		connectionHandler.sendToClient(MouseWheel(wheelRotation))
		nextInChain.dispatchMouseWheelEvent(wheelRotation)
	}

	override fun dispatchClipboardSync(msg: String) {
		connectionHandler.sendToClient(ClipboardSync(msg))
		nextInChain.dispatchClipboardSync(msg)
	}

	override fun dispatchEdgeSync(edge: Edge) {
		connectionHandler.sendToClient(EdgeSync(edge))
		nextInChain.dispatchEdgeSync(edge)
	}

	override fun dispatchCustomMsg(msg: Message) {
		connectionHandler.sendToClient(msg)
		nextInChain.dispatchCustomMsg(msg)
	}

	override fun dispatchKeyClick(keyCode: Int) {
		connectionHandler.sendToClient(KeyClick(keyCode))
		nextInChain.dispatchKeyClick(keyCode)
	}

	override fun chain(nextInChain: MessageDispatcher) {
		this.nextInChain = nextInChain
	}
}
