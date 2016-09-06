package com.github.krzychek.iomerge.server.daggerConfig

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.movementReader.VirtualScreen
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.github.krzychek.iomerge.server.network.MessageProcessorImpl
import com.github.krzychek.iomerge.server.network.ServerManager
import com.github.krzychek.iomerge.server.plugins.PluginLoader
import com.github.krzychek.iomerge.server.plugins.createChainOfType
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.google.common.eventbus.EventBus
import dagger.Module
import dagger.Provides
import org.annoprops.PropertyManager
import org.annoprops.PropertyManagerBuilder
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import javax.inject.Singleton

@Module
class MiscModule {

	@Provides @Singleton fun eventBus() = EventBus()

	@Provides @Singleton fun propertyManager(edgeTrigger: EdgeTrigger, serverManager: ServerManager, virtualScreen: VirtualScreen): PropertyManager
			= PropertyManagerBuilder().withObjects(edgeTrigger, serverManager, virtualScreen).build()

	@Provides @Singleton fun clipboard(): Clipboard = Toolkit.getDefaultToolkit().systemClipboard
}

@Module
class IfaceMappingModule {
	@Provides @Singleton fun messageProcessor(messageProcessorImpl: MessageProcessorImpl): MessageProcessor = messageProcessorImpl

	@Provides @Singleton fun appStateManager(appStateHolder: AppStateHolder): AppStateManager = appStateHolder

	@Provides @Singleton fun ioListenerChain(virtualScreen: VirtualScreen, pluginLoader: PluginLoader): IOListener {
		return (pluginLoader.getPluginObjectsOfType(IOListener::class.java) + virtualScreen)
				.createChainOfType(IOListener::class.java)
	}

	@Provides @Singleton fun messageDispatcherChain(messageDispatcherImpl: MessageDispatcherImpl, pluginLoader: PluginLoader): MessageDispatcher {
		return (pluginLoader.getPluginObjectsOfType(MessageDispatcher::class.java) + messageDispatcherImpl)
				.createChainOfType(MessageDispatcher::class.java)
	}
}