package pl.kbieron.iomerge.server.ui;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import pl.kbieron.iomerge.server.appState.AppStateListener;


public class UIModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EdgeTrigger.class).asEagerSingleton();
		bind(SettingsWindow.class).asEagerSingleton();
		bind(TrayManager.class).asEagerSingleton();

		Multibinder.newSetBinder(binder(), AppStateListener.class) //
				.addBinding().to(EdgeTrigger.class);
	}
}
