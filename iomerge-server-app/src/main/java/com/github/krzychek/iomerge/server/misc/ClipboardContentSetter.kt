package com.github.krzychek.iomerge.server.misc


import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class ClipboardContentSetter
@Inject constructor(private val systemClipboard: Clipboard) : ClipboardOwner {

	fun setClipboardContent(data: String) {
		systemClipboard.setContents(StringSelection(data), this)
	}

	override fun lostOwnership(clipboard: Clipboard, contents: Transferable) {
	}
}
