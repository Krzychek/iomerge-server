package pl.kbieron.iomerge.server;

import org.annoprops.PropertyManagerHelperBean;
import org.annoprops.springframework.SpringframeworkAnnopropsBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.AbstractApplicationContext;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;
import pl.kbieron.iomerge.server.api.movementReader.IOListener;
import pl.kbieron.iomerge.server.api.network.MessageDispatcher;
import pl.kbieron.iomerge.server.config.TinyLogConfigurator;
import pl.kbieron.iomerge.server.utils.plugins.PluginLoader;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pl.kbieron.iomerge.server.config.ConstantPaths.SETTINGS_FILE;
import static pl.kbieron.iomerge.server.utils.ChainHelper.createChain;


@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = "pl.kbieron.iomerge.server")
public class Bootstrap {

	public static void main(String... args) throws IOException {
		// should be invoked before spring context
		TinyLogConfigurator.configure(args);

		// array with spring config classes including plugins and bootstrap
		ArrayList<Class> classes = new ArrayList<>(new PluginLoader().loadPlugins());
		classes.add(Bootstrap.class);

		AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				classes.toArray(new Class<?>[classes.size()]));

		applicationContext.registerShutdownHook();
	}

	@Bean
	Clipboard clipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Bean
	PropertyManagerHelperBean propertyManager(ListableBeanFactory beanFactory) throws IOException {
		return SpringframeworkAnnopropsBeanFactory.createWithSpringFactory(beanFactory, SETTINGS_FILE);
	}

	@Bean
	@Primary
	IOListener ioListenerChain(List<IOListener> ioListeners) {
		return createChain(ioListeners, IOListener.class);
	}

	@Bean
	@Primary
	AppStateManager appStateManagerChain(List<AppStateManager> ioListeners) {
		return createChain(ioListeners, AppStateManager.class);
	}

	@Bean
	@Primary
	MessageDispatcher messageDispatcherChain(List<MessageDispatcher> ioListeners) {
		return createChain(ioListeners, MessageDispatcher.class);
	}
}
