package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.misc.ClipboardContentSetter
import com.github.krzychek.iomerge.server.misc.awtButton
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.model.MessageProcessorAdapter
import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton
import java.awt.MouseInfo
import java.awt.Robot
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Server implementation of [MessageProcessor]
 */

@Singleton class MessageProcessorImpl
@Inject constructor(private val clipboardContentSetter: ClipboardContentSetter, private val appStateManager: AppStateManager)
: MessageProcessorAdapter() {

	private val robot = Robot()

	override fun mouseMove(x: Int, y: Int) {
		MouseInfo.getPointerInfo().location.let {
			robot.mouseMove(it.x + x, it.y + y)
		}
	}

	override fun mousePress(button: MouseButton) = robot.mousePress(button.awtButton)

	override fun mouseRelease(button: MouseButton) = robot.mouseRelease(button.awtButton)

	override fun mouseWheel(rotation: Int) = robot.mouseWheel(rotation)

	override fun clipboardSync(text: String) = clipboardContentSetter.setClipboardContent(text)

	override fun returnToLocal(position: Float) = appStateManager.returnToLocal(position)
}
