package com.github.krzychek.iomerge.server.movementReader


import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.utils.IOListenerToAWTAdapter
import com.github.krzychek.iomerge.server.utils.makeInvisible
import com.google.common.eventbus.Subscribe
import org.springframework.stereotype.Component
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.util.*
import javax.swing.JFrame


/**
 * MovementReader based on transparent JFrame, catches mouse inside
 */
@Component
internal open class InvisibleInputReader(listener: IOListener,
										 private val appStateManager: AppStateManager,
										 private val mouseMovementReader: MouseMovementReader)
: JFrame("IOMerge MouseTrapReader") {


	init {
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
					mouseMovementReader.center = Point(it.x + it.width / 2, it.y + it.height / 2)
				}
	}

	@Synchronized private fun startReading() {
		isVisible = true
		mouseMovementReader.startReading()
	}

	@Synchronized private fun stopReading() {
		mouseMovementReader.stopReading()

		Thread.sleep(20) // ensure last move was read to not center mouse after restore

		appStateManager.restoreMouse()
		isVisible = false
	}

	@Subscribe
	fun onStateChange(newState: AppState) {
		when (newState) {
			AppState.ON_REMOTE -> startReading()
			else -> stopReading()
		}
	}
}
