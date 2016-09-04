@file:JvmName("Main")

package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.config.AppConfiguration
import com.github.krzychek.iomerge.server.config.AppConfigurator
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.movementReader.InvisibleInputReader
import com.github.krzychek.iomerge.server.movementReader.MouseMovementReader
import com.github.krzychek.iomerge.server.movementReader.VirtualScreen
import com.github.krzychek.iomerge.server.network.EventServer
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.github.krzychek.iomerge.server.network.MsgProcessor
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.github.krzychek.iomerge.server.ui.TrayManager
import com.github.krzychek.iomerge.server.utils.ClipboardSynchronizer
import com.github.krzychek.iomerge.server.utils.plugins.PluginLoader
import com.github.krzychek.iomerge.server.utils.plugins.createChainOfType
import com.google.common.eventbus.EventBus
import dagger.Component
import dagger.Module
import dagger.Provides
import org.annoprops.PropertyManager
import org.annoprops.PropertyManagerBuilder
import org.pmw.tinylog.Logger
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import javax.inject.Inject
import javax.inject.Singleton


fun main(vararg args: String) {
	val appConfiguration = AppConfigurator(*args).createConfig()

	val mainComponent: MainComponent = DaggerMainComponent.builder()
			.appConfiguration(appConfiguration)
			.build()

	mainComponent.init()
}

@Component(modules = arrayOf(MiscModule::class, IfaceMappingModule::class), dependencies = arrayOf(AppConfiguration::class))
@Singleton
interface MainComponent {
	fun init(): EagerSingletons
}

@Module
class MiscModule {

	@Provides @Singleton fun eventBus() = EventBus()

	@Provides @Singleton fun propertyManager(edgeTrigger: EdgeTrigger, eventServer: EventServer, virtualScreen: VirtualScreen): PropertyManager
			= PropertyManagerBuilder().withObjects(edgeTrigger, eventServer, virtualScreen).build()

	@Provides @Singleton fun clipboard(): Clipboard = Toolkit.getDefaultToolkit().systemClipboard
}

@Module
class IfaceMappingModule {
	@Provides @Singleton fun messageProcessor(msgProcessor: MsgProcessor): MessageProcessor = msgProcessor

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

@Singleton
class EventBusInitializer
@Inject constructor(eventBus: EventBus,
					clipboardSynchronizer: ClipboardSynchronizer,
					edgeTrigger: EdgeTrigger,
					eventServer: EventServer,
					invisibleInputReader: InvisibleInputReader,
					mouseMovementReader: MouseMovementReader) {
	init {
		eventBus.apply {
			register(clipboardSynchronizer)
			register(edgeTrigger)
			register(eventServer)
			register(invisibleInputReader)
			register(mouseMovementReader)
		}
	}
}

@Suppress("UNUSED_PARAMETER")
@Singleton
class EagerSingletons
@Inject constructor(eventBusInitializer: EventBusInitializer,
					clipboardSynchronizer: ClipboardSynchronizer,
					edgeTrigger: EdgeTrigger,
					eventServer: EventServer,
					appStateHolder: AppStateHolder,
					invisibleInputReader: InvisibleInputReader,
					mouseMovementReader: MouseMovementReader,
					trayManager: TrayManager,
					lifecycleManager: LifecycleManager) {
	init {
		lifecycleManager.init()
	}
}

@Singleton
class LifecycleManager @Inject constructor(private val appStateHolder: AppStateHolder,
										   private val propertyManager: PropertyManager,
										   private val appConfiguration: AppConfiguration) {

	var initialized = false
		private set
	var shuttingdown = false
		private set

	fun init() {
		if (initialized) {
			Logger.warn("App was already initialized")
			return
		}
		appConfiguration.startupCallback()

		propertyManager.readPropertiesFromFile(appConfiguration.settingsFile)

		appStateHolder.start()

		Runtime.getRuntime().addShutdownHook(Thread { shutdown(exitSystem = false) })

		initialized = true
	}

	fun shutdown(exitSystem: Boolean = true) {
		if (!shuttingdown) {
			propertyManager.savePropertiesToFile(appConfiguration.settingsFile)

			appStateHolder.shutdown()

			appConfiguration.shutdownCallback()

			// TODO show non daemon thread warning
			if (exitSystem) System.exit(0)
		}
	}
}