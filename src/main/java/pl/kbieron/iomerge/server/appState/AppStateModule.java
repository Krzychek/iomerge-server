package pl.kbieron.iomerge.server.appState;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import pl.kbieron.iomerge.server.utils.guice.Matchers;
import pl.kbieron.iomerge.server.utils.guice.MultibinderListener;


public class AppStateModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AppStateManager.class).to(AppStateHolder.class).asEagerSingleton();

		Multibinder<AppStateListener> appStateListenerMultibinder =
				Multibinder.newSetBinder(binder(), AppStateListener.class);

		bindListener(Matchers.subclassOf(AppStateListener.class), //
				new MultibinderListener(appStateListenerMultibinder));
	}
}
