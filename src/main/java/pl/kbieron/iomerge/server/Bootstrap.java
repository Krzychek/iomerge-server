package pl.kbieron.iomerge.server;

import org.annoprops.PropertyManager;
import org.kohsuke.args4j.CmdLineException;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.config.TinyLogConfigurator;

import javax.annotation.PreDestroy;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;

import static pl.kbieron.iomerge.server.config.ConstantPaths.SETTINGS_FILE;

@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = "pl.kbieron.iomerge.server")
public class Bootstrap {

	public static void main(String... args) throws IOException, CmdLineException {
		// should be invoked before spring context
		TinyLogConfigurator.configure(args);

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Bootstrap.class);
		applicationContext.registerShutdownHook();
	}

	@Bean
	Clipboard clipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Bean
	PropertyManager propertyManager(ListableBeanFactory beanFactory) throws IOException {
		PropertyManager propertyManager = PropertyManager.builder() //
				.withDefaultSerializers() //
				.withSpringListableBeanFactory(beanFactory) //
				.build();


		if (SETTINGS_FILE.exists()) {
			propertyManager.readPropertiesFromFile(SETTINGS_FILE);
		} else {
			Logger.info("File:" + SETTINGS_FILE + " does not exits, probably first run?");
		}

		return propertyManager;
	}

	@Component
	private static class ContextLifecycleWrapper {

		@Autowired
		private PropertyManager propertyManager;

		@PreDestroy
		private void shutdown() throws IOException {
			Logger.info("shutting down");
			propertyManager.savePropertiesToFile(SETTINGS_FILE);
		}
	}
}
