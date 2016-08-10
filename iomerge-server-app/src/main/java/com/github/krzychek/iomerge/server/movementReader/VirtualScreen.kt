package com.github.krzychek.iomerge.server.movementReader

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import org.annoprops.annotations.ConfigProperty
import org.annoprops.annotations.PropertyHolder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.geom.Point2D
import java.util.*


/**
 * Models device on server side, proxy between dispatcher msg dispatcher and whole module
 */
@Order(0)
@Component
@PropertyHolder
open class VirtualScreen(private val actionDispatcher: MessageDispatcher, private val appStateManager: AppStateManager) : IOListener {

	private lateinit var nextInChain: IOListener

	@ConfigProperty("MovementScale")
	var movementScale = 1.5

	@ConfigProperty("ReverseScroll")
	var reverseScroll = true

	private var unused = Point2D.Double()

	override fun move(dx: Int, dy: Int) {
		unused.x += dx * movementScale
		unused.y += dy * movementScale
		actionDispatcher.dispatchMouseMove(unused.x.toInt(), unused.y.toInt())
		unused.x %= 1.0
		unused.y %= 1.0

		nextInChain.move(dx, dy)
	}

	override fun keyTyped(e: KeyEvent) {
		nextInChain.keyTyped(e)
	}

	override fun mousePressed(e: MouseEvent) {
		actionDispatcher.dispatchMousePress(e.button)
		nextInChain.mousePressed(e)
	}

	override fun mouseReleased(e: MouseEvent) {
		actionDispatcher.dispatchMouseRelease(e.button)
		nextInChain.mouseReleased(e)
	}

	override fun mouseWheelMoved(e: MouseWheelEvent) {
		val wheelRotation = if (reverseScroll) -e.wheelRotation else e.wheelRotation
		actionDispatcher.dispatchMouseWheelEvent(wheelRotation)

		nextInChain.mouseWheelMoved(e)
	}

	override fun keyPressed(e: KeyEvent) {
		val keyCode = e.keyCode

		if (keyCode == KeyEvent.VK_F4)
			appStateManager.restoreMouse()
		else if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyPress(keyCode)
		else
			actionDispatcher.dispatchKeyClick(keyCode)

		nextInChain.keyPressed(e)
	}

	override fun keyReleased(e: KeyEvent) {
		val keyCode = e.keyCode

		if (Arrays.binarySearch(modKeys, keyCode) >= 0)
			actionDispatcher.dispatchKeyRelease(keyCode)

		nextInChain.keyReleased(e)
	}

	override fun mouseClicked(e: MouseEvent) {
		nextInChain.mouseClicked(e)
	}

	override fun chain(nextInChain: IOListener) {
		this.nextInChain = nextInChain
	}

	companion object {
		private val modKeys = intArrayOf(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT).apply { Arrays.sort(this) }
	}
}
