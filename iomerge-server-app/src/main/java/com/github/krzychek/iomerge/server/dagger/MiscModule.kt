package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.input.processors.MouseInputProcessor
import com.github.krzychek.iomerge.server.network.ServerManager
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.google.common.eventbus.EventBus
import dagger.Module
import dagger.Provides
import org.annoprops.PropertyManager
import org.annoprops.PropertyManagerBuilder
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.inject.Singleton

@Module
class MiscModule {

	@Provides @Singleton fun eventBus() = EventBus()

	@Provides @Singleton fun propertyManager(edgeTrigger: EdgeTrigger,
											 serverManager: ServerManager,
											 mouseInputProcessor: MouseInputProcessor)
			: PropertyManager = PropertyManagerBuilder().withObjects(edgeTrigger, serverManager, mouseInputProcessor).build()

	@Provides @Singleton fun clipboard(): Clipboard = Toolkit.getDefaultToolkit().systemClipboard

	@Provides @Singleton fun scheduledExecutorService(): ScheduledExecutorService = ScheduledThreadPoolExecutor(1)
}