package com.github.krzychek.iomerge.server.misc

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class ClipboardContentSetter
@Inject constructor(private val systemClipboard: Clipboard) {

	fun setClipboardContent(data: String) {
		systemClipboard.setContents(StringSelection(data), null)
	}
}
