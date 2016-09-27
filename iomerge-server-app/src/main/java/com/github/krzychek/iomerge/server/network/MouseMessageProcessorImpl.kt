package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.misc.awtButtonMask
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

	override fun mousePress(button: MouseButton) = robot.mousePress(button.awtButtonMask)

	override fun mouseRelease(button: MouseButton) = robot.mouseRelease(button.awtButtonMask)

	override fun mouseWheel(rotation: Int) = robot.mouseWheel(rotation)
}
