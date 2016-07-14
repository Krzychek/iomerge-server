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

		new PluginLoader().loadPluginsToContext(ctx);

		ctx.register(SpringConfig.class);

		ctx.registerShutdownHook();
		ctx.refresh();
	}

	private static void configureStaticContext(String[] args) {
		TinyLogConfigurator.configure(args);
	}
}
