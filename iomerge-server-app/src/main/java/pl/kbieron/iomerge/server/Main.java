package pl.kbieron.iomerge.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.kbieron.iomerge.server.config.TinyLogConfigurator;
import pl.kbieron.iomerge.server.utils.plugins.PluginLoader;

import java.io.IOException;


public class Main {

	public static void main(String... args) throws IOException {
		configureStaticContext(args);

		loadSpringContext();
	}

	private static void loadSpringContext() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		//noinspection ConfusingArgumentToVarargsMethod
		ctx.register(new PluginLoader().loadPlugins());
		ctx.register(SpringConfig.class);

		ctx.refresh();

		ctx.registerShutdownHook();
	}

	private static void configureStaticContext(String[] args) {
		TinyLogConfigurator.configure(args);
	}
}
