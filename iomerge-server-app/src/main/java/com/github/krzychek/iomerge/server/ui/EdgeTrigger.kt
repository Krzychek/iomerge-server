package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.misc.makeInvisible
import com.github.krzychek.iomerge.server.model.Edge
import com.google.common.eventbus.Subscribe
import org.annoprops.annotations.ConfigProperty
import java.awt.Dimension
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment
import java.awt.Point
import java.awt.Robot
import javax.inject.Inject
import javax.inject.Singleton
import javax.swing.JWindow
import javax.swing.Timer


/**
 * Enters remote device on mouse enter. displayed as 1px on border of screen
 */
@Singleton class EdgeTrigger
@Inject constructor(private val messageDispatcher: MessageDispatcher,
					private val appStateManager: AppStateManager)
: JWindow(), MouseRestoreListener {

	init {
		makeInvisible()
		isAutoRequestFocus = false
		isFocusable = false

		addMouseListener(MouseEnteredAdapter { appStateManager.enterRemoteScreen(this) })
	}

	private val THICKNESS = 1

	private val robot = Robot()

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

	private fun calculateLocation(): Point = getScreenBounds().run {
		when (edge) {
			Edge.LEFT -> minBy { it.x }!!
					.apply { translate(0, offset) }

			Edge.RIGHT -> maxBy { it.x }!!
					.apply { translate(width - THICKNESS, offset) }

			Edge.TOP -> minBy { it.y }!!
					.apply { translate(offset, 0) }

			Edge.BOTTOM -> maxBy { it.y }!!
					.apply { translate(offset, height - THICKNESS) }

			else -> throw IllegalStateException("Not handled edge: " + edge)
		}.location
	}


	private fun getScreenBounds() = getLocalGraphicsEnvironment().screenDevices.map { it.defaultConfiguration.bounds }

	@Subscribe
	fun onStateChange(appStateUpdateEvent: AppState) {
		when (appStateUpdateEvent) {
			AppState.ON_LOCAL -> {
				syncEdge()
				reposition()
				Timer(50) { actionEvent -> isVisible = true }.apply {
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
			syncEdge()
		}
	}

	fun syncEdge() {
		messageDispatcher.dispatchEdgeSync(edge)
	}

	override fun restoreMouseAt(position: Float) {
		when (edge) {
			Edge.RIGHT, Edge.LEFT -> robot.mouseMove(x, (y + height / position).toInt())
			Edge.TOP, Edge.BOTTOM -> robot.mouseMove((x + width / position).toInt(), y)
		}
	}
}