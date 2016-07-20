package com.github.krzychek.iomerge.server;

import com.github.krzychek.iomerge.server.config.AppPreConfigurator;
import com.github.krzychek.iomerge.server.utils.plugins.PluginLoader;
import com.sun.javafx.application.PlatformImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;


public class Main {

	public static void main(String... args) throws IOException {
		PlatformImpl.startup(() -> {});

		AppPreConfigurator appConfigurator = new AppPreConfigurator(args);
		appConfigurator.configure();

		loadSpringContext();
	}

	private static void loadSpringContext() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		new PluginLoader().loadPluginsToContext(ctx);

		ctx.register(SpringConfig.class);

		ctx.registerShutdownHook();
		ctx.refresh();
	}

}
