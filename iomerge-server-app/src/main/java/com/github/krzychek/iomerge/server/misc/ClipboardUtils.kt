package com.github.krzychek.iomerge.server.misc

import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.google.common.eventbus.Subscribe
import org.pmw.tinylog.Logger
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class ClipboardContentSetter
@Inject constructor(private val systemClipboard: Clipboard) {

	fun setClipboardContent(data: String) {
		systemClipboard.setContents(StringSelection(data), null)
	}
}

@Singleton class ClipboardSynchronizer
@Inject constructor(private val messageDispatcher: MessageDispatcher, private val clipboard: Clipboard) {

	private var sentData: String = ""

	@Subscribe
	fun onStateChange(newState: AppState) {
		if (AppState.ON_REMOTE == newState //
				&& clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

			try {
				val data = clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor)

				if (data is String && data != sentData) {
					messageDispatcher.dispatchClipboardSync(data)
					sentData = data
				}

			} catch (e: IOException) {
				Logger.warn(e)
			} catch (e: ClassCastException) {
				Logger.warn(e)
			}

		}
	}
}
