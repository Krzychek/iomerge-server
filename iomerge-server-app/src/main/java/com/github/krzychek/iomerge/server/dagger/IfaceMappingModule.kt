package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.model.processors.MessageProcessor
import com.github.krzychek.iomerge.server.model.processors.MiscMessageProcessor
import com.github.krzychek.iomerge.server.model.processors.MouseMessageProcessor
import com.github.krzychek.iomerge.server.network.MiscMessageProcessorImpl
import com.github.krzychek.iomerge.server.network.MouseMessageProcessorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class IfaceMappingModule {

	@Provides @Singleton fun messageProcessor(messageProcessorImpl: MiscMessageProcessorImpl,
											  mouseMessageProcessorImpl: MouseMessageProcessorImpl): MessageProcessor
			= object : MessageProcessor,
			MiscMessageProcessor by messageProcessorImpl,
			MouseMessageProcessor by mouseMessageProcessorImpl {
		override fun keyPress(character: Int) = throw UnsupportedOperationException("not implemented")
		override fun backBtnClick() = throw UnsupportedOperationException("not implemented")
		override fun homeBtnClick() = throw UnsupportedOperationException("not implemented")
		override fun keyRelease(character: Int) = throw UnsupportedOperationException("not implemented")
		override fun menuBtnClick() = throw UnsupportedOperationException("not implemented")
		override fun keyClick(keyCode: Int) = throw UnsupportedOperationException("not implemented")
	}

	@Provides @Singleton fun appStateManager(appStateHolder: AppStateHolder): AppStateManager = appStateHolder
}