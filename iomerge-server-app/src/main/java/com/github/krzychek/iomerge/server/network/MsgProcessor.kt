package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.model.MessageProcessor
import com.github.krzychek.iomerge.server.model.MessageProcessorAdapter
import com.github.krzychek.iomerge.server.utils.ClipboardContentSetter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * Server implementation of [MessageProcessor]
 */
@Component
internal open class MsgProcessor(private val clipboardContentSetter: ClipboardContentSetter, private val appStateManager: AppStateManager) : MessageProcessorAdapter() {

	override fun clipboardSync(text: String) = clipboardContentSetter.setClipboardContent(text)

	override fun returnToLocal(position: Float) = appStateManager.returnToLocal(position)
}
