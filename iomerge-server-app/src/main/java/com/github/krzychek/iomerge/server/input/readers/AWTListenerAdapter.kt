package com.github.krzychek.iomerge.server.input.readers

import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListener
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import java.awt.event.*


class AWTListenerAdapter(private val mouseListener: MouseListener, private val keyboardListener: KeyboardListener)
: MouseWheelListener, java.awt.event.MouseListener, KeyListener {

	override fun mouseWheelMoved(e: MouseWheelEvent) = mouseListener.mouseWheelMoved(e)

	override fun mouseClicked(e: MouseEvent) = mouseListener.mouseClicked(e)

	override fun mousePressed(e: MouseEvent) = mouseListener.mousePressed(e)

	override fun mouseReleased(e: MouseEvent) = mouseListener.mouseReleased(e)

	override fun mouseEntered(e: MouseEvent) {
	}

	override fun mouseExited(e: MouseEvent) {
	}

	override fun keyTyped(e: KeyEvent) = keyboardListener.keyTyped(e)

	override fun keyPressed(e: KeyEvent) = keyboardListener.keyPressed(e)

	override fun keyReleased(e: KeyEvent) = keyboardListener.keyReleased(e)
}
