package pl.kbieron.iomerge.server.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.network.MsgDispatcher;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * Manages system clipboard, syncs clipboard on remote entering
 */
@Component
public class ClipboardManager implements ClipboardOwner {

	private static final Logger log = Logger.getLogger(ClipboardManager.class);

	@Autowired
	private MsgDispatcher msgDispatcher;

	@Autowired
	private Clipboard systemClipboard;

	private Object sentData;

	@EventListener
	public void onStateChange(AppState newState) {
		if ( AppState.ON_REMOTE == newState //
				&& systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor) ) {

			try {
				String data = (String) systemClipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);

				if ( data != null && !data.equals(sentData) ) {
					msgDispatcher.dispatchClipboardSync(data);
					sentData = data;
				}

			} catch (UnsupportedFlavorException | IOException | ClassCastException e) {
				log.warn(e);
			}
		}
	}

	public void setClipboardContent(String data) {
		systemClipboard.setContents(new StringSelection(data), this);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable transferable) {}
}
