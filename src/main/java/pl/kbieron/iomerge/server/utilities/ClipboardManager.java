package pl.kbieron.iomerge.server.utilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppState;
import pl.kbieron.iomerge.server.network.RemoteMsgDispatcher;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


@Component
public class ClipboardManager implements ApplicationListener<AppState.UpdateEvent>, ClipboardOwner {

	private static Logger log = Logger.getLogger(ClipboardManager.class);

	@Autowired
	private RemoteMsgDispatcher remoteMsgDispatcher;

	@Autowired
	private Clipboard systemClipboard;

	private Object sentData;

	@Override
	public void onApplicationEvent(AppState.UpdateEvent updateEvent) {
		if ( AppState.ON_REMOTE == updateEvent.getStateChange() //
				&& systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor) ) {

			try {
				String data = (String) systemClipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);

				if ( data != null && !data.equals(sentData) ) {
					remoteMsgDispatcher.dispatchClipboardSync(data);
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
