package pl.kbieron.iomerge.server.ui;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;


class TrayManager {

	private static final Logger log = Logger.getLogger(TrayManager.class);

	private SystemTray tray;

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private SettingsWindow settingsWindow;

	private TrayIcon trayIcon;

	@PostConstruct
	private void init() {
		if ( !SystemTray.isSupported() ) {
			log.warn("Tray is not supported on this system");
			return;
		}
		tray = SystemTray.getSystemTray();
		try {
			trayIcon = new TrayIcon(ImageIO.read(context.getResource("icon.gif").getURL()));
			trayIcon.setImageAutoSize(true);
		} catch (IOException e) {
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
			context.close();
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
