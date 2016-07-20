package com.github.krzychek.server.ui;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;


@Component
class TrayManager {

	private final SettingsWindow settingsWindow;
	private SystemTray tray;
	private TrayIcon trayIcon;

	@Autowired
	public TrayManager(SettingsWindow settingsWindow) {
		this.settingsWindow = settingsWindow;

		if (!SystemTray.isSupported()) {
			Logger.error("Tray is not supported on this system");
			return;
		}
		tray = SystemTray.getSystemTray();
		try {
			trayIcon = new TrayIcon(ImageIO.read(getClass().getResource("/icon.png")), "IOMerge");
			trayIcon.setImageAutoSize(true);
		} catch (Exception e) {
			Logger.error("filed to load tray icon", e);
			return;
		}
		createAndShow();
	}

	private void createAndShow() {
		tray.remove(trayIcon);

		PopupMenu popup = new PopupMenu();
		popup.add(new MenuItem("Settings")).addActionListener(e -> settingsWindow.show());
		popup.add(new MenuItem("Exit")).addActionListener(e -> Runtime.getRuntime().exit(0));

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}
}
