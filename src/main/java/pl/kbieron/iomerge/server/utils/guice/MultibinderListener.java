package pl.kbieron.iomerge.server.utils.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;


public class MultibinderListener implements TypeListener {

	private final Multibinder<AppStateListener> appStateListenerMultibinder;

	public MultibinderListener(Multibinder<AppStateListener> appStateListenerMultibinder) {
		this.appStateListenerMultibinder = appStateListenerMultibinder;
	}

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		appStateListenerMultibinder.addBinding().to(EdgeTrigger.class);
	}
}
