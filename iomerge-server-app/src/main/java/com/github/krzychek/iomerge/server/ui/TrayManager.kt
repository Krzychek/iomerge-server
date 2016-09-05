package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.daggerConfig.LifecycleManager
import dagger.Lazy
import dorkbox.systemTray.SystemTray
import org.pmw.tinylog.Logger
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.TrayIcon
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class TrayManager
@Inject constructor(lazySettingsWindow: Lazy<SettingsWindow>, val lifecycleManager: LifecycleManager) {

	val settingsWindow: SettingsWindow by lazy { lazySettingsWindow.get() }


	private fun tryLoadNativeTray() {
		SystemTray.getSystemTray()!!.apply {

			status = "IOMerge"

			setIcon("trayIcon", javaClass.getResourceAsStream("/icon.png"))

			addMenuEntry("Settings", { systemTray, menuEntry -> settingsWindow.show() })
			addMenuEntry("Exit", { systemTray, menuEntry -> lifecycleManager.shutdown() })
		}
	}

	private fun loadAwtTray() {

		val image = ImageIO.read(javaClass.getResource("/icon.png"))

		val trayIcon = TrayIcon(image, "IOMerge").apply {
			isImageAutoSize = true
			popupMenu = PopupMenu().apply {
				add(MenuItem("Settings")).addActionListener { settingsWindow.show() }
				add(MenuItem("Exit")).addActionListener { lifecycleManager.shutdown() }
			}
		}
		java.awt.SystemTray.getSystemTray().add(trayIcon)
	}

	init {

		var loaded = false
		try {
			tryLoadNativeTray()
			loaded = true
		} catch (e: Throwable) {
			Logger.warn(e, "Problems with loading native tray, trying awt.")
		}

		if (!loaded) try {
			loadAwtTray()
		} catch (e: Throwable) {
			Logger.warn(e, "Filed to load awt tray")
		}
	}
}