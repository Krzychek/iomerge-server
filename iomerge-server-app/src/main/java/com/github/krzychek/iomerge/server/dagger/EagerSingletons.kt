package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.misc.ClipboardSynchronizer
import com.github.krzychek.iomerge.server.movementReader.InvisibleInputReader
import com.github.krzychek.iomerge.server.network.ServerManager
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.github.krzychek.iomerge.server.ui.TrayManager
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNUSED_PARAMETER")
@Singleton class EagerSingletons
@Inject constructor(eventBusInitializer: EventBusInitializer,
					appStateHolder: AppStateHolder,
					serverManager: ServerManager,
					edgeTrigger: EdgeTrigger,
					invisibleInputReader: InvisibleInputReader,
					clipboardSynchronizer: ClipboardSynchronizer,
					trayManager: TrayManager,
					lifecycleManager: LifecycleManager) {
	init {
		lifecycleManager.init()
	}
}