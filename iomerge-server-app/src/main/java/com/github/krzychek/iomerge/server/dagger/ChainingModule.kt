package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListener
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.input.processors.KeyboardInputProcessor
import com.github.krzychek.iomerge.server.input.processors.MouseInputProcessor
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.github.krzychek.iomerge.server.plugins.PluginLoader
import com.github.krzychek.iomerge.server.plugins.convertToChainOfType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

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