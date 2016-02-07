package pl.kbieron.iomerge.server.appState;

import com.google.inject.AbstractModule;


public class AppStateModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AppStateManager.class).to(AppStateHolder.class).asEagerSingleton();
	}
}
