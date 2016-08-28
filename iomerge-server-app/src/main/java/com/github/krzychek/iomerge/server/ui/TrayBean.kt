package com.github.krzychek.iomerge.server.ui

import org.pmw.tinylog.Logger
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


@Component
open class TrayBean(private val ctx: AbstractApplicationContext) {

	val settingsWindow: SettingsWindow by lazy { ctx.getBean(SettingsWindow::class.java) }

	init {
		if (SystemTray.isSupported()) {

			val image = try {
				ImageIO.read(javaClass.getResource("/icon.png"))
			} catch (e: Exception) {
				Logger.error("filed to load tray icon, using blank image", e)
				BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY)
			}

			val trayIcon = TrayIcon(image, "IOMerge").apply {
				isImageAutoSize = true
				popupMenu = PopupMenu().apply {
					add(MenuItem("Settings")).addActionListener { settingsWindow.show() }
					add(MenuItem("Exit")).addActionListener { ctx.close() }
				}
			}

			try {
				SystemTray.getSystemTray().add(trayIcon)
			} catch (e: AWTException) {
				Logger.error("TrayIcon could not be added.")
			}

		} else {
			Logger.error("Tray is not supported on this system")
		}
	}
}
