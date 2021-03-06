package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.input.readers.InvisibleInputReader
import com.github.krzychek.iomerge.server.input.readers.MouseMovementReader
import com.github.krzychek.iomerge.server.misc.ClipboardSynchronizer
import com.github.krzychek.iomerge.server.network.ServerManager
import com.github.krzychek.iomerge.server.ui.EdgeTrigger
import com.google.common.eventbus.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class EventBusInitializer
@Inject constructor(eventBus: EventBus,
					clipboardSynchronizer: ClipboardSynchronizer,
					edgeTrigger: EdgeTrigger,
					serverManager: ServerManager,
					invisibleInputReader: InvisibleInputReader,
					mouseMovementReader: MouseMovementReader) {
	init {
		eventBus.apply {
			register(clipboardSynchronizer)
			register(edgeTrigger)
			register(serverManager)
			register(invisibleInputReader)
			register(mouseMovementReader)
		}
	}
}