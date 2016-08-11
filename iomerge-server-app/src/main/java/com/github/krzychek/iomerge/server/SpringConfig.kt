package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.config.AppConfigurator.Companion.settingsFile
import com.github.krzychek.iomerge.server.utils.ChainHelper
import com.sun.javafx.application.PlatformImpl
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
	open fun ioListenerChain(ioListeners: List<IOListener>): IOListener {
		return ChainHelper.createChain(ioListeners, IOListener::class.java)
	}

	@Bean
	@Primary
	open fun appStateManagerChain(ioListeners: List<AppStateManager>): AppStateManager {
		return ChainHelper.createChain(ioListeners, AppStateManager::class.java)
	}

	@Bean
	@Primary
	open fun messageDispatcherChain(ioListeners: List<MessageDispatcher>): MessageDispatcher {
		return ChainHelper.createChain(ioListeners, MessageDispatcher::class.java)
	}
}