package pl.kbieron.iomerge.server;

import org.annoprops.PropertyManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;

@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = "pl.kbieron.iomerge.server")
public class SpringBootstrap {

	private static final Logger log = Logger.getLogger(SpringBootstrap.class);

	private static final File SETTINGS_FILE = new File(System.getProperty("user.home") + File.separator + ".config/iomerge.properties");

	public static void main(String... args) throws IOException {
		new AnnotationConfigApplicationContext(SpringBootstrap.class);
	}

	@Bean
	Clipboard clipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Bean
	PropertyManager propertyManager(ListableBeanFactory beanFactory) throws IOException {
		PropertyManager propertyManager = PropertyManager.builder() //
				.withDefaultSerializers() //
				.withSpring(beanFactory) //
				.build();


		if (SETTINGS_FILE.exists()) {
			propertyManager.readPropertiesFromFile(SETTINGS_FILE);
		} else {
			log.info("File:" + SETTINGS_FILE + " does not exits, probably first run?");
		}

		return propertyManager;
	}

	@Component
	private static class ContextLifecycleWrapper {

		@Autowired
		private PropertyManager propertyManager;

		@PreDestroy
		private void shutdown() throws IOException {
			log.info("shutting down");
			try {
				propertyManager.savePropertiesToFile(SETTINGS_FILE);
			} catch (IOException e) {
				log.error("Problem while saving settings file", e);
			}
		}
	}
}
