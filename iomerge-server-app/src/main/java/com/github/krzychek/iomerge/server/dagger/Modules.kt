package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListener
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.input.processors.KeyboardInputProcessor
import com.github.krzychek.iomerge.server.input.processors.MouseInputProcessor
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.github.krzychek.iomerge.server.network.MessageProcessorImpl
import com.github.krzychek.iomerge.server.network.ServerManager
import com.github.krzychek.iomerge.server.plugins.PluginLoader
import com.github.krzychek.iomerge.server.plugins.convertToChainOfType
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

	@Provides @Singleton fun propertyManager(edgeTrigger: EdgeTrigger, serverManager: ServerManager, mouseInputProcessor: MouseInputProcessor): PropertyManager
			= PropertyManagerBuilder().withObjects(edgeTrigger, serverManager, mouseInputProcessor).build()

	@Provides @Singleton fun clipboard(): Clipboard = Toolkit.getDefaultToolkit().systemClipboard
}

@Module
class IfaceMappingModule {

	@Provides @Singleton fun messageProcessor(messageProcessorImpl: MessageProcessorImpl): MessageProcessor = messageProcessorImpl

	@Provides @Singleton fun appStateManager(appStateHolder: AppStateHolder): AppStateManager = appStateHolder
}

@Module
class ChainingModule {

	@Provides @Singleton fun mouseListenerChain(mouseInputProcessor: MouseInputProcessor, pluginLoader: PluginLoader): MouseListener {
		return (pluginLoader.getPluginObjectsOfType(MouseListener::class.java) + mouseInputProcessor)
				.convertToChainOfType(MouseListener::class.java)
	}

	@Provides @Singleton fun keyboardListenerChain(keyboardInputProcessor: KeyboardInputProcessor, pluginLoader: PluginLoader): KeyboardListener {
		return (pluginLoader.getPluginObjectsOfType(KeyboardListener::class.java) + keyboardInputProcessor)
				.convertToChainOfType(KeyboardListener::class.java)
	}

	@Provides @Singleton fun messageDispatcherChain(messageDispatcherImpl: MessageDispatcherImpl, pluginLoader: PluginLoader): MessageDispatcher {
		return (pluginLoader.getPluginObjectsOfType(MessageDispatcher::class.java) + messageDispatcherImpl)
				.convertToChainOfType(MessageDispatcher::class.java)
	}
}