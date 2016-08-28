package com.github.krzychek.iomerge.server.appState

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManagerAdapter
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener
import com.google.common.eventbus.EventBus
import org.pmw.tinylog.Logger
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*


/**
 * Holds state of application, and publish state events on change
 */
@Order(0)
@Component
open class AppStateHolder(private val eventBus: EventBus) : AppStateManagerAdapter() {

	private val mouseRestoreListeners = HashSet<MouseRestoreListener>()
	private var state: AppState = AppState.STARTUP
	private var position: Float = 0f

	@EventListener(ContextRefreshedEvent::class)
	@Synchronized private fun onContextRefreshed() {
		if (state == AppState.STARTUP) setNewState(AppState.DISCONNECTED)
	}

	override fun enterRemoteScreen(mouseRestoreListener: MouseRestoreListener) {
		mouseRestoreListeners.add(mouseRestoreListener)
		setNewState(AppState.ON_REMOTE)
		nextInChain.enterRemoteScreen(mouseRestoreListener)
	}

	override fun restoreMouse() {
		mouseRestoreListeners.forEach { it.restoreMouseAt(position) }
		mouseRestoreListeners.clear()

		nextInChain.restoreMouse()
	}

	override fun connected() {
		setNewState(AppState.ON_LOCAL)
		nextInChain.connected()
	}

	override fun returnToLocal(position: Float) {
		this.position = position
		setNewState(AppState.ON_LOCAL)

		nextInChain.returnToLocal(position)
	}

	override fun disconnected() {
		setNewState(AppState.DISCONNECTED)
		nextInChain.disconnected()
	}

	@Synchronized private fun setNewState(newState: AppState) {
		if (state != newState) {
			Logger.info("setting application state to: " + newState)
			state = newState
			eventBus.post(state)
		}
	}
}
