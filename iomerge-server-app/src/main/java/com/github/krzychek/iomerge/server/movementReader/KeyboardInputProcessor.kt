package com.github.krzychek.iomerge.server.movementReader

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListenerAdapter
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import java.awt.event.KeyEvent
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class KeyboardInputProcessor
@Inject constructor(private val messageDispatcher: MessageDispatcher, private val appStateManager: AppStateManager)
: KeyboardListenerAdapter() {


	override fun keyTyped(e: KeyEvent) {
		nextInChain.keyTyped(e)
	}

	override fun keyPressed(e: KeyEvent) {
		val keyCode = e.keyCode

		if (keyCode == KeyEvent.VK_F4)
			appStateManager.restoreMouse()
		else if (keyCode.isModKey())
			messageDispatcher.dispatchKeyPress(keyCode)
		else
			messageDispatcher.dispatchKeyClick(keyCode)

		nextInChain.keyPressed(e)
	}

	override fun keyReleased(e: KeyEvent) {
		val keyCode = e.keyCode

		if (keyCode.isModKey())
			messageDispatcher.dispatchKeyRelease(keyCode)

		nextInChain.keyReleased(e)
	}


	private val MOD_KEYS = intArrayOf(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT).apply { Arrays.sort(this) }
	private fun Int.isModKey() = MOD_KEYS.binarySearch(this) >= 0

}
