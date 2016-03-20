package pl.kbieron.iomerge.server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.annoprops.PropertyManager;
import org.apache.log4j.Logger;
import pl.kbieron.iomerge.server.appState.AppStateModule;
import pl.kbieron.iomerge.server.gesture.GestureModule;
import pl.kbieron.iomerge.server.movementReader.MovementReaderModule;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.network.NetworkModule;
import pl.kbieron.iomerge.server.ui.EdgeTrigger;
import pl.kbieron.iomerge.server.ui.UIModule;
import pl.kbieron.iomerge.server.utils.UtilModule;

import java.io.File;
import java.io.IOException;


public class GuiceBootstrap extends AbstractModule {

	private static final Logger log = Logger.getLogger(GuiceBootstrap.class);

	private static final String SETTINGS_FILE;

	static {
		SETTINGS_FILE = System.getProperty("user.home") + File.separator + ".config/iomerge.properties";
	}

	public static void main(String... args) throws InterruptedException, IOException {
		GuiceBootstrap guiceBootstrap = new GuiceBootstrap();
		Injector injector = Guice.createInjector(guiceBootstrap);
		guiceBootstrap.init(injector);
	}

	private void init(Injector injector) throws IOException {

		EventServer eventServer = injector.getInstance(EventServer.class);
		EdgeTrigger edgeTrigger = injector.getInstance(EdgeTrigger.class);

		final PropertyManager propertyManager = PropertyManager.builder() //
				.withObject(eventServer) //
				.withObject(edgeTrigger) //
				.withDefaultSerializers() //
				.build();

		// start
		if ( new File(SETTINGS_FILE).exists() )
			propertyManager.readPropertiesFromFile(SETTINGS_FILE);

		eventServer.start();

		// shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("shutting down");
			try {
				propertyManager.savePropertiesToFile(SETTINGS_FILE);
			} catch (IOException e) {
				log.error("Problem while saving settings file", e);
			}
		}, "shutdown hook"));
	}

	@Override
	protected void configure() {
		install(new MovementReaderModule());
		install(new AppStateModule());
		install(new NetworkModule());
		install(new UIModule());
		install(new UtilModule());
		install(new GestureModule());
	}
}
