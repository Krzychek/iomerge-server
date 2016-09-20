package com.github.krzychek.iomerge.server.network

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.misc.ClipboardContentSetter
import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.model.processors.MiscMessageProcessor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Server implementation of [MiscMessageProcessor]
 */
@Singleton class MiscMessageProcessorImpl
@Inject constructor(private val clipboardContentSetter: ClipboardContentSetter,
					private val appStateManager: AppStateManager)
: MiscMessageProcessor {

	override fun edgeSync(edge: Edge?) = Unit

	override fun clipboardSync(text: String) = clipboardContentSetter.setClipboardContent(text)

	override fun returnToLocal(position: Float) = appStateManager.returnToLocal(position)
}