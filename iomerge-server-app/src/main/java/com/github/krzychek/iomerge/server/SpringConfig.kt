package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.config.AppConfigurator
import com.github.krzychek.iomerge.server.config.AppConfigurator.Paths.settingsFile
import com.github.krzychek.iomerge.server.utils.plugins.PluginLoader
import com.github.krzychek.iomerge.server.utils.plugins.createChainOfType
import org.annoprops.PropertyManagerHelperBean
import org.annoprops.springframework.SpringframeworkAnnopropsBeanFactory
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard


@Configuration
@ComponentScan(basePackages = arrayOf("com.github.krzychek.iomerge.server"))
open class SpringConfig {
	@Bean
	open fun clipboard(): Clipboard {
		return Toolkit.getDefaultToolkit().systemClipboard
	}

	@Bean
	open fun robot(): Robot {
		return Robot()
	}

	@Bean
	open fun propertyManager(beanFactory: ListableBeanFactory): PropertyManagerHelperBean {
		return SpringframeworkAnnopropsBeanFactory.createWithSpringFactory(beanFactory, settingsFile)
	}

	@Bean
	@Primary
	open fun ioListenerChain(ioListener: IOListener, pluginLoader: PluginLoader): IOListener {
		return (pluginLoader.getPluginObjectsOfType(IOListener::class.java) + ioListener)
				.createChainOfType(IOListener::class.java)
	}

	@Bean
	@Primary
	open fun appStateManagerChain(appStateManager: AppStateManager, pluginLoader: PluginLoader): AppStateManager {
		return (pluginLoader.getPluginObjectsOfType(AppStateManager::class.java) + appStateManager)
				.createChainOfType(AppStateManager::class.java)
	}

	@Bean
	@Primary
	open fun messageDispatcherChain(messageDispatcher: MessageDispatcher, pluginLoader: PluginLoader): MessageDispatcher {
		return (pluginLoader.getPluginObjectsOfType(MessageDispatcher::class.java) + messageDispatcher)
				.createChainOfType(MessageDispatcher::class.java)
	}

	@Bean
	open fun appConfigurator() = AppConfigurator

}
