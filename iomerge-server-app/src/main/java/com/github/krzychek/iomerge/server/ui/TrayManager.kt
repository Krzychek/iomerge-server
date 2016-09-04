package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.LifecycleManager
import dagger.Lazy
import org.pmw.tinylog.Logger
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TrayManager
@Inject constructor(private val lazySettingsWindow: Lazy<SettingsWindow>, lifecycleManager: LifecycleManager) {

	val settingsWindow: SettingsWindow by lazy { lazySettingsWindow.get() }

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
					add(MenuItem("Exit")).addActionListener { lifecycleManager.shutdown() }
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
