package com.github.krzychek.iomerge.server.ui

import org.pmw.tinylog.Logger
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.awt.*
import java.awt.image.BufferedImage
import javax.annotation.PostConstruct
import javax.imageio.ImageIO


@Component
open class TrayBean(private val context: AbstractApplicationContext) {

	val settingsWindow: SettingsWindow
		get() = context.getBean(SettingsWindow::class.java)

	@PostConstruct
	fun init() {

		if (SystemTray.isSupported().not()) {
			Logger.error("Tray is not supported on this system")
			return
		}

		val image = try {
			ImageIO.read(javaClass.getResource("/icon.png"))
		} catch (e: Exception) {
			Logger.error("filed to load tray icon, using blank image", e)
			BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY)
		}

		val trayIcon = TrayIcon(image, "IOMerge").apply {
			isImageAutoSize = true
			popupMenu = PopupMenu().apply {
				add(MenuItem("Settings")).addActionListener { e -> settingsWindow.show() }
				add(MenuItem("Exit")).addActionListener { e -> Runtime.getRuntime().exit(0) }
			}
		}

		try {
			SystemTray.getSystemTray().add(trayIcon)
		} catch (e: AWTException) {
			Logger.error("TrayIcon could not be added.")
		}
	}

}
