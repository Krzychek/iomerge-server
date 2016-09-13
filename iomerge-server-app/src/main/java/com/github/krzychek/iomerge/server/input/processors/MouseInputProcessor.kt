package com.github.krzychek.iomerge.server.input.processors

import com.github.krzychek.iomerge.server.api.Order
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListenerAdapter
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton
import org.annoprops.annotations.ConfigProperty
import java.awt.geom.Point2D
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Models device on server side, proxy between dispatcher msg dispatcher and whole module
 */
@Order(0)
@Singleton class MouseInputProcessor
@Inject constructor(private val messageDispatcher: MessageDispatcher)
: MouseListenerAdapter() {

	@ConfigProperty("MovementScale")
	var movementScale = 1.5

	@ConfigProperty("ReverseScroll")
	var reverseScroll = true

	private var unused = Point2D.Double()

	override fun move(dx: Int, dy: Int) {
		unused.x += dx * movementScale
		unused.y += dy * movementScale
		messageDispatcher.dispatchMouseMove(unused.x.toInt(), unused.y.toInt())
		unused.x %= 1.0
		unused.y %= 1.0

		nextInChain.move(dx, dy)
	}

	override fun mousePressed(button: MouseButton) {
		messageDispatcher.dispatchMousePress(button)
		nextInChain.mousePressed(button)
	}

	override fun mouseReleased(button: MouseButton) {
		messageDispatcher.dispatchMouseRelease(button)
		nextInChain.mouseReleased(button)
	}

	override fun mouseWheelMoved(wheelRotation: Int) {
		messageDispatcher.dispatchMouseWheelEvent(
				if (reverseScroll) -wheelRotation else wheelRotation)

		nextInChain.mouseWheelMoved(wheelRotation)
	}

	override fun mouseClicked(button: MouseButton) {
		// TODO
		nextInChain.mouseClicked(button)
	}
}
