package com.github.krzychek.iomerge.server.utils;


import com.github.krzychek.iomerge.server.api.appState.AppState;
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * Manages system clipboard, syncs clipboard on remote entering
 */
@Component
public class ClipboardSynchronizer implements ClipboardOwner {


	private final MessageDispatcher messageDispatcher;
	private final Clipboard systemClipboard;

	private Object sentData;

	@Autowired
	ClipboardSynchronizer(MessageDispatcher messageDispatcher, Clipboard systemClipboard) {
		this.messageDispatcher = messageDispatcher;
		this.systemClipboard = systemClipboard;
	}

	@EventListener
	public void onStateChange(AppState newState) {
		if (AppState.ON_REMOTE == newState //
			&& systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

			try {
				String data = (String) systemClipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);

				if (data != null && !data.equals(sentData)) {
					messageDispatcher.dispatchClipboardSync(data);
					sentData = data;
				}

			} catch (UnsupportedFlavorException | IOException | ClassCastException e) {
				Logger.warn(e);
			}
		}
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable transferable) {}
}
