package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.misc.notImplementedProxy
import com.github.krzychek.iomerge.server.model.processors.*
import com.github.krzychek.iomerge.server.network.MiscMessageProcessorImpl
import com.github.krzychek.iomerge.server.network.MouseMessageProcessorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class IfaceMappingModule {

	@Provides @Singleton fun agregateMessageProcessors(messageProcessorImpl: MiscMessageProcessorImpl,
													   mouseMessageProcessorImpl: MouseMessageProcessorImpl): MessageProcessor
			= object : MessageProcessor,
			MiscMessageProcessor by messageProcessorImpl,
			MouseMessageProcessor by mouseMessageProcessorImpl,
			AndroidMessageProcessor by AndroidMessageProcessor::class.java.notImplementedProxy(),
			KeyboardMessageProcessor by KeyboardMessageProcessor::class.java.notImplementedProxy() {
	}

	@Provides @Singleton fun appStateManager(appStateHolder: AppStateHolder): AppStateManager = appStateHolder
}