package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListener
import com.github.krzychek.iomerge.server.api.inputListeners.MouseListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.input.processors.KeyboardInputProcessor
import com.github.krzychek.iomerge.server.input.processors.MouseInputProcessor
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.github.krzychek.iomerge.server.plugins.PluginLoader
import com.github.krzychek.iomerge.server.plugins.convertToChain
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChainingModule {

	@Provides @Singleton fun mouseListenerChain(mouseInputProcessor: MouseInputProcessor, pluginLoader: PluginLoader): MouseListener
			= (pluginLoader.getPluginObjectsOfType<MouseListener>() + mouseInputProcessor).convertToChain()

	@Provides @Singleton fun keyboardListenerChain(keyboardInputProcessor: KeyboardInputProcessor, pluginLoader: PluginLoader): KeyboardListener
			= (pluginLoader.getPluginObjectsOfType<KeyboardListener>() + keyboardInputProcessor).convertToChain()

	@Provides @Singleton fun messageDispatcherChain(messageDispatcherImpl: MessageDispatcherImpl, pluginLoader: PluginLoader): MessageDispatcher
			= (pluginLoader.getPluginObjectsOfType<MessageDispatcher>() + messageDispatcherImpl).convertToChain()
}