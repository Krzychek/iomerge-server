package com.github.krzychek.iomerge.server.movementReader


import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.utils.ReentrantLockCondition
import com.github.krzychek.iomerge.server.utils.ThrottledCall
import org.springframework.stereotype.Component
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


/**
 * MovementReader based on transparent JFrame, catches mouse inside
 */
@Component
open class MouseMovementReader(private val listener: IOListener) {

	private val robot = Robot()
	private val isReadingCond = ReentrantLockCondition()

	@Volatile private var reading: Boolean = false
	@Volatile var center = Point()

	fun startReading() = try {
		isReadingCond.lock()
		reading = true
		isReadingCond.signal()
	} finally {
		isReadingCond.unlock()
	}

	fun stopReading() {
		reading = false
	}


	@PostConstruct
	fun init() = thread.start()

	private fun centerMousePointer() {
		robot.mouseMove(center.x, center.y)
	}

	@PreDestroy
	private fun shutdown() {
		reading = false
		thread.cancel()
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

	private val thread = object : Thread("MouseTrapReader : mouse move reading thread") {
		private var canceled: Boolean = false

		fun cancel() {
			canceled = true
		}

		override fun run() {

			while (!canceled) {

				isReadingCond.awaitLocked()
				while (reading) readMove()
			}
		}
	}

}
