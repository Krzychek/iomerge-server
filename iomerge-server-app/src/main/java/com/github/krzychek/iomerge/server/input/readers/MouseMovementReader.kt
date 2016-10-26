package com.github.krzychek.iomerge.server.input.readers


import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.misc.PausableTaskExecutor
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * MovementReader based on transparent JFrame, catches mouse inside
 */
@Singleton class MouseMovementReader
@Inject constructor(private val listener: MouseListener) {

	private val robot = Robot()

	private val readingExecutorService = PausableTaskExecutor().apply {
		scheduleWithFixedDelay(task = { readMove() }, delay = 15, unit = TimeUnit.MILLISECONDS, addPaused = false)
	}

	@Volatile var center = Point()

	fun startReading() {
		centerMousePointer()
		readingExecutorService.resume()
	}

	fun stopReading() {
		readingExecutorService.pause()
	}


	private fun centerMousePointer() {
		robot.mouseMove(center.x, center.y)
	}

	private fun readMove() {
		MouseInfo.getPointerInfo().location.let {
			it.translate(-center.x, -center.y) // relative to center

			if (it.x != 0 || it.y != 0) {
				listener.move(it.x, it.y)
				centerMousePointer()
			}
		}
	}
}
