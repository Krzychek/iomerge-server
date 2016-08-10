package com.github.krzychek.iomerge.server.movementReader


import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.utils.BooleanCondition
import com.github.krzychek.iomerge.server.utils.IOListenerToAWTAdapter
import com.github.krzychek.iomerge.server.utils.makeInvisible
import org.pmw.tinylog.Logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.awt.GraphicsEnvironment
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Robot
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.swing.JFrame


/**
 * MovementReader based on transparent JFrame, catches mouse inside
 */
@Component
internal open class MouseTrapReader(private val listener: IOListener, private val appStateManager: AppStateManager) : JFrame("IOMerge MovementReader") {

	private val robot = Robot()
	private val readingThread = MoveReadingThread()

	@Volatile private var center = Point()
	private val isReading = BooleanCondition()

	@PostConstruct
	private fun init() {
		readingThread.start()
		// UI stuff
		reposition()
		makeInvisible()
		isAutoRequestFocus = true

		IOListenerToAWTAdapter(listener).let {
			addMouseWheelListener(it)
			addMouseListener(it)
			addKeyListener(it)
		}
	}

	private fun reposition() {
		GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
				.map { it.defaultConfiguration.bounds }
				.maxWith(Comparator { a, b -> a.width * a.height - b.width * b.height })!!
				.let {
					setLocation(it.x, it.y)
					setSize(it.width, it.height)
					center = Point(it.x + it.width / 2, it.y + it.height / 2)
				}
	}

	private fun readMove() {
		if (isReading.isTrue.not()) {
			Logger.warn("readMove called, but I'm not reading")
			return
		}

		MouseInfo.getPointerInfo().location.apply {
			translate(-center.x, -center.y) // translte for relative location of mouse
		}.apply {
			if (x != 0 || y != 0) {
				listener.move(x, y)
				centerMousePointer()
			}
		}

	}

	private fun centerMousePointer() {
		robot.mouseMove(center.x, center.y)
	}

	@Synchronized private fun startReading() {
		if (isReading.isTrue) return

		isVisible = true

		center = location.apply {
			translate(width / 2, height / 2)
		}

		centerMousePointer()

		isReading.isTrue = true
	}

	@Synchronized private fun stopReading() {
		if (isReading.isTrue.not()) return
		isReading.isTrue = false
		try {
			Thread.sleep(10) // ensure last move was read to not center mouse after restore
		} catch (e: InterruptedException) {
			throw IllegalStateException(e)
		}

		appStateManager.restoreMouse()
		isVisible = false
	}

	@EventListener
	fun onStateChange(newState: AppState) {
		when (newState) {
			AppState.ON_REMOTE -> startReading()
			else -> stopReading()
		}
	}

	@PreDestroy
	private fun shutdown() {
		stopReading()
		readingThread.interrupt()
	}


	private inner class MoveReadingThread : Thread("MouseTrapReader : mouse move reading thread") {

		@Volatile private var interrupted = false

		override fun run() {

			while (interrupted.not()) {

				isReading.await()
				while (isReading.isTrue) readMove()
			}
		}

		override fun interrupt() {
			interrupted = true
			super.interrupt()
		}
	}

}
