package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.utils.makeInvisible
import org.annoprops.annotations.ConfigProperty
import org.annoprops.annotations.PropertyHolder
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.awt.GraphicsEnvironment
import java.awt.Robot
import java.util.*
import javax.annotation.PostConstruct
import javax.swing.JWindow
import javax.swing.Timer


/**
 * Enters remote device on mouse enter. displayed as 1px on border of screen
 */
@PropertyHolder
@Component
open class EdgeTrigger(private val messageDispatcher: MessageDispatcher, private val appStateManager: AppStateManager, private val robot: Robot)
: JWindow(), MouseRestoreListener {

	@ConfigProperty("Edge")
	var edge = Edge.LEFT
		private set

	@ConfigProperty("EdgeTriggerOffset")
	var offset = 200
		private set

	@ConfigProperty("EdgeTriggerLength")
	var length = 500
		private set

	@PostConstruct
	private fun init() {
		reposition()
		makeInvisible()
		isAutoRequestFocus = false
		isFocusable = false

		addMouseListener(MouseEnteredAdapter { appStateManager.enterRemoteScreen(this) })
	}

	private fun reposition() {
		val displayRect = when (edge) {
			Edge.LEFT -> GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
					.map { it.defaultConfiguration.bounds }
					.minWith(Comparator { a, b -> a.x - b.x })!!
					.apply { translate(0, offset) }


			Edge.RIGHT -> GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
					.map { it.defaultConfiguration.bounds }
					.maxWith(Comparator { a, b -> a.x - b.x })!!
					.apply { translate(width - this@EdgeTrigger.width, offset) }

			else -> throw IllegalStateException("Not handled edge: " + edge)

		}

		val wasVisible = isVisible
		isVisible = false

		displayRect.let {
			location = it.location
			size = it.size
		}

		isVisible = wasVisible

	}

	@EventListener
	fun onStateChange(appStateUpdateEvent: AppState) {
		when (appStateUpdateEvent) {
			AppState.ON_LOCAL -> {
				messageDispatcher.dispatchEdgeSync(edge)
				reposition()
				Timer(50) { actionEvent -> isVisible = true }
						.apply {
							isRepeats = false
							start()
						}
			}
			else -> isVisible = false
		}
	}

	fun setProperties(edge: Edge, length: Int, offset: Int) {
		if (edge != this.edge || length != this.length || offset != this.offset) {
			this.edge = edge
			this.offset = offset
			this.length = length
			reposition()
			messageDispatcher.dispatchEdgeSync(edge)
		}
	}

	override fun restoreMouseAt(position: Float) {
		robot.mouseMove(x, y + (height / position).toInt())
	}
}
