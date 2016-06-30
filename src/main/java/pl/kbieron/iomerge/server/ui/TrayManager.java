package pl.kbieron.iomerge.server.ui;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;

@Component
class TrayManager {


	private SystemTray tray;

	@Autowired
	private SettingsWindow settingsWindow;

	@Autowired
	private AbstractApplicationContext applicationContext;

	private TrayIcon trayIcon;

	public TrayManager() {
		if (!SystemTray.isSupported()) {
			Logger.error("Tray is not supported on this system");
			return;
		}
		tray = SystemTray.getSystemTray();
		try {
			trayIcon = new TrayIcon(ImageIO.read(getClass().getResource("/icon.png")));
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
		popup.add(new MenuItem("Settings")).addActionListener(e -> settingsWindow.setVisible(true));
		popup.add(new MenuItem("Exit")).addActionListener(e -> Runtime.getRuntime().exit(0));

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}
}
