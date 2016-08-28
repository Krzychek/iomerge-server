package com.github.krzychek.iomerge.server.utils


import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.google.common.eventbus.Subscribe
import org.pmw.tinylog.Logger
import org.springframework.stereotype.Component
import java.awt.datatransfer.*
import java.io.IOException


/**
 * Manages system clipboard, syncs clipboard on remote entering
 */
@Component
open class ClipboardSynchronizer(private val messageDispatcher: MessageDispatcher, private val systemClipboard: Clipboard) : ClipboardOwner {

	private var sentData: String = ""

	@Subscribe
	fun onStateChange(newState: AppState) {
		if (AppState.ON_REMOTE == newState //
				&& systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

			try {
				val data = systemClipboard.getContents(this).getTransferData(DataFlavor.stringFlavor) as String

				if (data != sentData) {
					messageDispatcher.dispatchClipboardSync(data)
					sentData = data
				}

			} catch (e: UnsupportedFlavorException) {
				Logger.warn(e)
			} catch (e: IOException) {
				Logger.warn(e)
			} catch (e: ClassCastException) {
				Logger.warn(e)
			}

		}
	}

	override fun lostOwnership(clipboard: Clipboard, transferable: Transferable) {
	}
}
