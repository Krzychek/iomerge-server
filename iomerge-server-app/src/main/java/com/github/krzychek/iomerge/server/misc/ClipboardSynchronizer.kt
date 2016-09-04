package com.github.krzychek.iomerge.server.misc


import com.github.krzychek.iomerge.server.api.appState.AppState
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.google.common.eventbus.Subscribe
import org.pmw.tinylog.Logger
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Manages system clipboard, syncs clipboard on remote entering
 */
@Singleton class ClipboardSynchronizer
@Inject constructor(private val messageDispatcher: MessageDispatcher, private val systemClipboard: Clipboard) : ClipboardOwner {

	private var sentData: String = ""

	@Subscribe
	fun onStateChange(newState: AppState) {
		if (AppState.ON_REMOTE == newState //
				&& systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

			try {
				val data = systemClipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)

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

	override fun lostOwnership(clipboard: Clipboard, transferable: Transferable) {
	}
}
