package pl.kbieron.iomerge.server.utils;

import org.apache.log4j.Logger;
import javax.inject.Inject;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.appState.AppStateListener;
import pl.kbieron.iomerge.server.network.MsgDispatcher;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class ClipboardManager implements AppStateListener, ClipboardOwner {

	private static final Logger log = Logger.getLogger(ClipboardManager.class);

	@Inject
	private MsgDispatcher msgDispatcher;

	@Inject
	private Clipboard systemClipboard;

	private Object sentData;

	@Override
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
