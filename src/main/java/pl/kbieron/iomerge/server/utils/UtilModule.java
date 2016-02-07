package pl.kbieron.iomerge.server.utils;

import com.google.inject.AbstractModule;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;


public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ClipboardManager.class).asEagerSingleton();

		bind(Clipboard.class).toInstance(Toolkit.getDefaultToolkit().getSystemClipboard());
	}
}
