package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.misc.ClipboardContentSetter
import com.github.krzychek.iomerge.server.model.SpecialKey
import com.github.krzychek.iomerge.server.model.processors.KeyboardMessageProcessor
import java.awt.Robot
import java.awt.event.KeyEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class KeyboardMessageProcessorImpl
@Inject constructor(private val clipboard: ClipboardContentSetter) : KeyboardMessageProcessor {
	private val robot = Robot()
	override fun stringTyped(string: String) {
		clipboard.setClipboardContent(string)
		robot.apply {
			keyPress(KeyEvent.VK_CONTROL)
			keyPress(KeyEvent.VK_V)
			keyRelease(KeyEvent.VK_V)
			keyRelease(KeyEvent.VK_CONTROL)
		}
	}

	override fun keyPress(character: Int)
			= robot.keyPress(character.getKeyCode())

	override fun keyRelease(character: Int)
			= robot.keyRelease(character.getKeyCode())

	override fun keyClick(character: Int) {
		val keyCode = character.getKeyCode()
		robot.keyPress(keyCode)
		robot.keyRelease(keyCode)
	}

	override fun specialKeyClick(specialKey: SpecialKey) {
		val keyCode = specialKey.getKeyCode()
		robot.keyPress(keyCode)
		robot.keyRelease(keyCode)
	}

	private fun Int.getKeyCode(): Int = KeyEvent.getExtendedKeyCodeForChar(this)

	private fun SpecialKey.getKeyCode() = when (this) {
		SpecialKey.BACKSPACE -> KeyEvent.VK_BACK_SPACE
	}
}