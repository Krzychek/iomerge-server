package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.utilities.ClipboardManager;

import static pl.kbieron.iomerge.model.RemoteMsgTypes.CLIPBOARD_SYNC;
import static pl.kbieron.iomerge.model.RemoteMsgTypes.REMOTE_EXIT;


@Component
class RemoteMsgProcessor {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private ClipboardManager clipboardManager;

	public void process(byte[] actionBytes) {
		switch (actionBytes[0]) {
			case REMOTE_EXIT:
				appStateManager.exitRemote();
				break;
			case CLIPBOARD_SYNC:
				clipboardManager.setClipboardContent(new String(actionBytes, 1, actionBytes.length - 1));
		}
	}
}
