package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.model.MessageProcessorAdapter
import com.github.krzychek.iomerge.server.utils.ClipboardContentSetter
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Server implementation of [MessageProcessor]
 */

@Singleton class MsgProcessor
@Inject constructor(private val clipboardContentSetter: ClipboardContentSetter, private val appStateManager: AppStateManager) : MessageProcessorAdapter() {

	override fun clipboardSync(text: String) = clipboardContentSetter.setClipboardContent(text)

	override fun returnToLocal(position: Float) = appStateManager.returnToLocal(position)
}
