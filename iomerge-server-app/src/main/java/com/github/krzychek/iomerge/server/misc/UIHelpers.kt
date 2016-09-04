package com.github.krzychek.iomerge.server.misc

import com.sun.javafx.application.PlatformImpl
import javafx.application.Platform
import java.awt.Color
import java.awt.Point
import java.awt.Toolkit
import java.awt.Window
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel


fun Window.makeInvisible() {
	isAlwaysOnTop = true
	background = Color(0, 0, 0, 0)
	type = Window.Type.POPUP
}

fun JFrame.makeInvisible() {
	isUndecorated = true

	isAlwaysOnTop = true
	background = Color(0, 0, 0, 0)
	type = Window.Type.POPUP

	contentPane = JPanel().apply {
		isOpaque = false
		cursor = Toolkit.getDefaultToolkit().createCustomCursor(BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), Point(0, 0), "blank")
	}
}

object JFXHelper {
	@Volatile private var initialized: Boolean = false

	fun runLater(cbk: () -> Unit) {
		if (!initialized) {
			PlatformImpl.startup { }
			initialized = true
		}
		Platform.runLater(cbk)
	}


}