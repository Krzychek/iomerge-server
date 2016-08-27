package com.github.krzychek.iomerge.server.movementReader


import com.github.krzychek.iomerge.server.api.movementReader.IOListener
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

	private val monitor = Object()

	@Volatile private var reading: Boolean = false
		set(value) {
			field = value
			synchronized(monitor) { monitor.notifyAll() }
		}

	@Volatile var center = Point()

	fun startReading() {
		centerMousePointer()
		reading = true
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
		thread.interrupt()
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
		private val monitor = Object()

		override fun run() {
			while (true) {

				try {
					synchronized(monitor) { monitor.wait() }
					while (reading) readMove()

				} catch (e: InterruptedException) {
					return
				}
			}
		}
	}

}
