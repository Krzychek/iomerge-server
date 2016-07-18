package com.github.krzychek.server;

import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.api.movementReader.IOListener;
import com.github.krzychek.server.api.network.MessageDispatcher;
import org.annoprops.PropertyManagerHelperBean;
import org.annoprops.springframework.SpringframeworkAnnopropsBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.List;

import static com.github.krzychek.server.config.ConstantPaths.SETTINGS_FILE;
import static com.github.krzychek.server.utils.ChainHelper.createChain;


@Configuration
@ComponentScan(basePackages = "com.github.krzychek.server")
class SpringConfig {

	@Bean
	Clipboard clipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Bean
	Robot robot() throws AWTException {
		return new Robot();
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
