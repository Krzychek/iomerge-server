package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.properties.PropertyManager;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Component
public class Bootstrap {

	private final String SETTINGS_FILE = "settings.properties";

	@Autowired
	private PropertyManager propertyManager;

	@Autowired
	private EventServer eventServer;

	public static void main(String... args) throws InterruptedException {
		new ClassPathXmlApplicationContext("/spring.xml");
	}

	@PostConstruct
	private void init() throws IOException {
		propertyManager.readPropertiesFromFile(SETTINGS_FILE);

		eventServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	private void shutdown() {
		propertyManager.savePropertiesToFile(SETTINGS_FILE);
	}
}
