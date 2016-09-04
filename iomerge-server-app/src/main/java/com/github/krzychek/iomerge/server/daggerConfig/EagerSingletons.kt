package com.github.krzychek.iomerge.server.daggerConfig

import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.misc.ClipboardSynchronizer
import com.github.krzychek.iomerge.server.movementReader.InvisibleInputReader
import com.github.krzychek.iomerge.server.movementReader.MouseMovementReader
import com.github.krzychek.iomerge.server.network.EventServer
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.github.krzychek.iomerge.server.ui.TrayManager
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNUSED_PARAMETER")
@Singleton class EagerSingletons
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