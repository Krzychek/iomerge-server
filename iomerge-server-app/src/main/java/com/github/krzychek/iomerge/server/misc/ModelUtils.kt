package com.github.krzychek.iomerge.server.misc

import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton
import java.awt.event.InputEvent
import java.awt.event.MouseEvent

val MouseButton.awtButton: Int get() = when (this) {
	MouseButton.LEFT -> InputEvent.BUTTON1_DOWN_MASK
	MouseButton.CENTER -> InputEvent.BUTTON2_DOWN_MASK
	MouseButton.RIGHT -> InputEvent.BUTTON3_DOWN_MASK
}
val MouseEvent.mouseButton: MouseButton get() = when (this.button) {
	InputEvent.BUTTON1_DOWN_MASK -> MouseButton.LEFT
	InputEvent.BUTTON2_DOWN_MASK -> MouseButton.CENTER
	InputEvent.BUTTON3_DOWN_MASK -> MouseButton.RIGHT
	else -> throw IllegalStateException("wrong awt button")
}