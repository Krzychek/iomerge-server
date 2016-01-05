package pl.kbieron.iomerge.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.network.EventServer;
import pl.kbieron.iomerge.server.properties.PropertyManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;


@Component
class Bootstrap {

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
	}

	@PreDestroy
	private void shutdown() {
		propertyManager.savePropertiesToFile(SETTINGS_FILE);
	}
}
