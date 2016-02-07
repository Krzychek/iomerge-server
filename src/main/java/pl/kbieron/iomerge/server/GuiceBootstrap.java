package pl.kbieron.iomerge.server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import org.annoprops.PropertyManager;
import org.apache.log4j.Logger;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.appState.AppStateModule;
import pl.kbieron.iomerge.server.gesture.GestureModule;
import pl.kbieron.iomerge.server.movementReader.MouseTrapReader;
import pl.kbieron.iomerge.server.movementReader.MovementReaderModule;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.network.NetworkModule;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;
import pl.kbieron.iomerge.server.ui.UIModule;
import pl.kbieron.iomerge.server.utils.ClipboardManager;
import pl.kbieron.iomerge.server.utils.UtilModule;

import java.io.IOException;
import java.util.Arrays;


public class GuiceBootstrap extends AbstractModule {

	private static final Logger log = Logger.getLogger(AbstractModule.class);

	private static final String SETTINGS_FILE = "settings.properties";

	public static void main(String... args) throws InterruptedException, IOException {
		Injector injector = Guice.createInjector(new GuiceBootstrap());

		final PropertyManager propertyManager = PropertyManager.createWithObjects(
				Arrays.asList(injector.getInstance(EventServer.class), injector.getInstance(EdgeTrigger.class)));

		propertyManager.readPropertiesFromFile(SETTINGS_FILE);
		injector.getInstance(EventServer.class).start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				propertyManager.savePropertiesToFile(SETTINGS_FILE);
			} catch (IOException e) {
				log.error(e);
			}
		}));
	}

	@Override
	protected void configure() {
		install(new MovementReaderModule());
		install(new AppStateModule());
		install(new MovementReaderModule());
		install(new NetworkModule());
		install(new UIModule());
		install(new UtilModule());
		install(new GestureModule());

		Multibinder<AppStateListener> appStateListenersBinder =
				Multibinder.newSetBinder(binder(), AppStateListener.class);
		appStateListenersBinder.addBinding().to(EdgeTrigger.class);
		appStateListenersBinder.addBinding().to(MouseTrapReader.class);
		appStateListenersBinder.addBinding().to(ClipboardManager.class);
	}
}
