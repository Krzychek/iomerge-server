package pl.kbieron.iomerge.server.ui;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;


class TrayManager {

	private static final Logger log = Logger.getLogger(TrayManager.class);

	private SystemTray tray;

	private SettingsWindow settingsWindow;

	private TrayIcon trayIcon;

	@Inject
	public TrayManager(SettingsWindow settingsWindow) {
		this.settingsWindow = settingsWindow;
		init();
	}

	private void init() {
		if ( !SystemTray.isSupported() ) {
			log.warn("Tray is not supported on this system");
			return;
		}
		tray = SystemTray.getSystemTray();
		try {
			trayIcon = new TrayIcon(ImageIO.read(getClass().getResource("/icon.png")));
			trayIcon.setImageAutoSize(true);
		} catch (Exception e) {
			log.error("filed to load tray icon", e);
			return;
		}
		createAndShow();
	}

	private void createAndShow() {
		tray.remove(trayIcon);

		PopupMenu popup = new PopupMenu();
		popup.add(new MenuItem("Settings")).addActionListener(e -> settingsWindow.setVisible(true));
		popup.add(new MenuItem("Exit")).addActionListener(e -> {
			//			context.close();
			System.exit(0);
		});

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}
}
