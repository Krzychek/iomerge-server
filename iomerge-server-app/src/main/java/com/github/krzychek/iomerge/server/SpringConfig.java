package com.github.krzychek.iomerge.server;

import com.github.krzychek.iomerge.server.api.appState.AppStateManager;
import com.github.krzychek.iomerge.server.api.movementReader.IOListener;
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher;
import com.github.krzychek.iomerge.server.config.ConstantPaths;
import com.github.krzychek.iomerge.server.utils.ChainHelper;
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


@Configuration
@ComponentScan(basePackages = "com.github.krzychek.iomerge.server")
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
		return SpringframeworkAnnopropsBeanFactory.createWithSpringFactory(beanFactory, ConstantPaths.SETTINGS_FILE);
	}

	@Bean
	@Primary
	IOListener ioListenerChain(List<IOListener> ioListeners) {
		return ChainHelper.createChain(ioListeners, IOListener.class);
	}

	@Bean
	@Primary
	AppStateManager appStateManagerChain(List<AppStateManager> ioListeners) {
		return ChainHelper.createChain(ioListeners, AppStateManager.class);
	}

	@Bean
	@Primary
	MessageDispatcher messageDispatcherChain(List<MessageDispatcher> ioListeners) {
		return ChainHelper.createChain(ioListeners, MessageDispatcher.class);
	}
}
