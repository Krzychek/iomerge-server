package com.github.krzychek.iomerge.server.appState

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.appState.MouseRestoreListener
import com.google.common.eventbus.EventBus
import org.pmw.tinylog.Logger
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Holds state of application, and publish state events on change
 */

@Singleton class AppStateHolder
@Inject constructor(private val eventBus: EventBus) : AppStateManager {

	private val mouseRestoreListeners = HashSet<MouseRestoreListener>()
	private var state: AppState = AppState.STARTUP
	private var position: Float = 0f

	fun start() {
		if (state == AppState.STARTUP) setNewState(AppState.DISCONNECTED)
		else throw IllegalStateException("State on start should be ${AppState.STARTUP}, but was $state")
	}

	fun shutdown() {
		if (state != AppState.SHUTDOWN) setNewState(AppState.SHUTDOWN)
		else Logger.error("State is already ${AppState.SHUTDOWN}")
	}

	override fun enterRemoteScreen(mouseRestoreListener: MouseRestoreListener) {
		mouseRestoreListeners.add(mouseRestoreListener)
		setNewState(AppState.ON_REMOTE)
	}

	override fun restoreMouse() {
		mouseRestoreListeners.forEach { it.restoreMouseAt(position) }
		mouseRestoreListeners.clear()
	}

	override fun connected() {
		setNewState(AppState.ON_LOCAL)
	}

	override fun returnToLocal(position: Float) {
		this.position = position
		setNewState(AppState.ON_LOCAL)
	}

	override fun disconnected() {
		setNewState(AppState.DISCONNECTED)
	}

	@Synchronized private fun setNewState(newState: AppState) {
		if (state != newState) {
			Logger.info("setting application state to: " + newState)
			state = newState
			eventBus.post(state)
		}
	}
}
