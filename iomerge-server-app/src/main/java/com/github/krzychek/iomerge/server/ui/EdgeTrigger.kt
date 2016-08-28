package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.utils.makeInvisible
import com.google.common.eventbus.Subscribe
import org.annoprops.annotations.ConfigProperty
import org.annoprops.annotations.PropertyHolder
import org.springframework.stereotype.Component
import java.awt.Dimension
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment
import java.awt.Point
import java.awt.Robot
import java.util.*
import javax.swing.JWindow
import javax.swing.Timer


/**
 * Enters remote device on mouse enter. displayed as 1px on border of screen
 */
@PropertyHolder
@Component
open class EdgeTrigger(private val messageDispatcher: MessageDispatcher, private val appStateManager: AppStateManager, private val robot: Robot)
: JWindow(), MouseRestoreListener {


	init {
		makeInvisible()
		isAutoRequestFocus = false
		isFocusable = false

		addMouseListener(MouseEnteredAdapter { appStateManager.enterRemoteScreen(this) })
	}

	private val THICKNESS = 1

	@ConfigProperty("Edge")
	var edge = Edge.LEFT
		private set

	@ConfigProperty("EdgeTriggerOffset")
	var offset = 200
		private set

	@ConfigProperty("EdgeTriggerLength")
	var length = 500
		private set


	private fun reposition() {
		val wasVisible = isVisible
		isVisible = false

		size = calculateSize()
		location = calculateLocation()

		isVisible = wasVisible
	}

	private fun calculateSize(): Dimension = when (edge) {
		Edge.LEFT, Edge.RIGHT -> Dimension(THICKNESS, length)
		Edge.TOP, Edge.BOTTOM -> Dimension(length, THICKNESS)
		else -> throw IllegalStateException("Not handled edge: " + edge)
	}

	private fun calculateLocation(): Point = when (edge) {
		Edge.LEFT -> getScreenBounds()
				.minWith(Comparator { a, b -> a.x - b.x })!!
				.apply { translate(0, offset) }

		Edge.RIGHT -> getScreenBounds()
				.maxWith(Comparator { a, b -> a.x - b.x })!!
				.apply { translate(width - THICKNESS, offset) }

		Edge.TOP -> getScreenBounds()
				.minWith(Comparator { a, b -> a.y - b.y })!!
				.apply { translate(offset, 0) }

		Edge.BOTTOM -> getScreenBounds()
				.maxWith(Comparator { a, b -> a.y - b.y })!!
				.apply { translate(offset, height - THICKNESS) }

		else -> throw IllegalStateException("Not handled edge: " + edge)
	}.location


	private fun getScreenBounds() = getLocalGraphicsEnvironment().screenDevices.map { it.defaultConfiguration.bounds }

	@Subscribe
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
		when (edge) {
			Edge.RIGHT, Edge.LEFT -> robot.mouseMove(x, (y + height / position).toInt())
			Edge.TOP, Edge.BOTTOM -> robot.mouseMove((x + width / position).toInt(), y)
		}
	}
}