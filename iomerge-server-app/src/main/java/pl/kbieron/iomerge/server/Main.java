package pl.kbieron.iomerge.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import pl.kbieron.iomerge.server.config.TinyLogConfigurator;
import pl.kbieron.iomerge.server.utils.plugins.PluginLoader;

import java.io.IOException;
import java.util.ArrayList;


public class Main {

	public static void main(String... args) throws IOException {
		configureStaticContext(args);

		AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(getSpringConfigurationClasses());

		applicationContext.registerShutdownHook();
	}

	private static void configureStaticContext(String[] args) {
		TinyLogConfigurator.configure(args);
	}

	private static Class<?>[] getSpringConfigurationClasses() {
		ArrayList<Class> classes = new ArrayList<>(new PluginLoader().loadPlugins());
		classes.add(SpringConfig.class);
		return classes.toArray(new Class[classes.size()]);
	}
}
