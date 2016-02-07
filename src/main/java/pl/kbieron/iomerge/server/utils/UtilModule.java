package pl.kbieron.iomerge.server.utils;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import pl.kbieron.iomerge.server.appState.AppStateListener;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;


public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ClipboardManager.class).asEagerSingleton();

		Multibinder.newSetBinder(binder(), AppStateListener.class)//
				.addBinding().to(ClipboardManager.class);

		bind(Clipboard.class).toInstance(Toolkit.getDefaultToolkit().getSystemClipboard());
	}
}
