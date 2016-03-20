package pl.kbieron.iomerge.server.ui;

import com.google.inject.AbstractModule;


public class UIModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EdgeTrigger.class).asEagerSingleton();
		bind(SettingsWindow.class).asEagerSingleton();
		bind(TrayManager.class).asEagerSingleton();
	}
}
