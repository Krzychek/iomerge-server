package com.github.krzychek.iomerge.server.input.processors

import com.github.krzychek.iomerge.server.api.Order
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.inputListeners.KeyboardListenerAdapter
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import java.awt.event.KeyEvent
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Order(0)
@Singleton class KeyboardInputProcessor
@Inject constructor(private val messageDispatcher: MessageDispatcher, private val appStateManager: AppStateManager)
: KeyboardListenerAdapter() {


	override fun keyTyped(keyCode: Int) {
		nextInChain.keyTyped(keyCode)
	}

	override fun keyPressed(keyCode: Int) {
		if (keyCode == KeyEvent.VK_F4)
			appStateManager.restoreMouse()
		else if (keyCode.isModKey())
			messageDispatcher.dispatchKeyPress(keyCode)
		else
			messageDispatcher.dispatchKeyClick(keyCode)

		nextInChain.keyPressed(keyCode)
	}

	override fun keyReleased(keyCode: Int) {
		if (keyCode.isModKey())
			messageDispatcher.dispatchKeyRelease(keyCode)

		nextInChain.keyReleased(keyCode)
	}


	private val MOD_KEYS = intArrayOf(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT).apply { Arrays.sort(this) }
	private fun Int.isModKey() = MOD_KEYS.binarySearch(this) >= 0

}
