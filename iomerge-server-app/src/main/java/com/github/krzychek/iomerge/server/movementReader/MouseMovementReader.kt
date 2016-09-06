package com.github.krzychek.iomerge.server.movementReader


import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.misc.NotifyingValueHolder
import com.github.krzychek.iomerge.server.misc.ThrottledCall
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import javax.inject.Inject
import javax.inject.Singleton


/**
 * MovementReader based on transparent JFrame, catches mouse inside
 */
@Singleton class MouseMovementReader
@Inject constructor(private val listener: MouseListener) {

	private val robot = Robot()

	private val isReading = NotifyingValueHolder(false)

	@Volatile var center = Point()

	fun startReading() {
		centerMousePointer()
		isReading.value = true
	}

	fun stopReading() {
		isReading.value = false
	}


	private fun centerMousePointer() {
		robot.mouseMove(center.x, center.y)
	}

	private val readMove = ThrottledCall(15) {
		MouseInfo.getPointerInfo().location.let {
			it.translate(-center.x, -center.y) // relative to center

			if (it.x != 0 || it.y != 0) {
				listener.move(it.x, it.y)
				centerMousePointer()
			}
		}
	}

	private val thread = object : Thread("MouseMovementReader : mouse move reading thread") {
		override fun run() {
			while (true) {

				try {
					isReading.waitToChange()
					while (isReading.value) readMove()

				} catch (e: InterruptedException) {
					return
				}
			}
		}
	}.apply {
		isDaemon = true
		start()
	}
}
