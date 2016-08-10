package com.github.krzychek.iomerge.server.utils

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
