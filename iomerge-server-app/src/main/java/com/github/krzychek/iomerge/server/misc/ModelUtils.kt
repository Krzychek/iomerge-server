package com.github.krzychek.iomerge.server.misc

import com.github.krzychek.iomerge.server.model.MouseButton
import org.pmw.tinylog.Logger
import java.awt.event.InputEvent
import java.awt.event.MouseEvent

val MouseButton.awtButtonMask: Int get() = when (this) {
	MouseButton.LEFT -> InputEvent.BUTTON1_DOWN_MASK
	MouseButton.CENTER -> InputEvent.BUTTON2_DOWN_MASK
	MouseButton.RIGHT -> InputEvent.BUTTON3_DOWN_MASK
}
val MouseEvent.mouseButton: MouseButton get() = when (this.button) {
	MouseEvent.BUTTON1 -> MouseButton.LEFT
	MouseEvent.BUTTON2 -> MouseButton.CENTER
	MouseEvent.BUTTON3 -> MouseButton.RIGHT
	else -> {
		Logger.warn("unrecognized button: ${this.button}, faalling back to LEFT")
		MouseButton.LEFT
	}
}