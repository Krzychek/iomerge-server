package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.misc.awtButton
import com.github.krzychek.iomerge.server.model.MouseButton
import com.github.krzychek.iomerge.server.model.processors.MouseMessageProcessor
import java.awt.MouseInfo
import java.awt.Robot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class MouseMessageProcessorImpl
@Inject constructor() : MouseMessageProcessor {

	private val robot = Robot()

	override fun mouseMove(x: Int, y: Int) {
		MouseInfo.getPointerInfo().location.let {
			robot.mouseMove(it.x + x, it.y + y)
		}
	}

	override fun mousePress(button: MouseButton) = robot.mousePress(button.awtButton)

	override fun mouseRelease(button: MouseButton) = robot.mouseRelease(button.awtButton)

	override fun mouseWheel(rotation: Int) = robot.mouseWheel(rotation)
}
