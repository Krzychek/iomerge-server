package com.github.krzychek.iomerge.server.input.readers

import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListener
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.misc.mouseButton
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelListener

fun MouseListener.asAWTMouseListener() = object : java.awt.event.MouseListener {
	override fun mouseClicked(e: MouseEvent) = mouseClicked(e.mouseButton)

	override fun mousePressed(e: MouseEvent) = mousePressed(e.mouseButton)

	override fun mouseReleased(e: MouseEvent) = mouseReleased(e.mouseButton)

	override fun mouseEntered(e: MouseEvent) = Unit

	override fun mouseExited(e: MouseEvent) = Unit
}

fun MouseListener.asAWTMouseWheelListener() = MouseWheelListener { e -> mouseWheelMoved(e.wheelRotation) }

fun KeyboardListener.asAWTKeyListener() = object : KeyListener {
	override fun keyTyped(e: KeyEvent) = keyTyped(e.keyCode)

	override fun keyPressed(e: KeyEvent) = keyPressed(e.keyCode)

	override fun keyReleased(e: KeyEvent) = keyReleased(e.keyCode)
}