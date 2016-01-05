package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.MessageProcessorAdapter;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.utilities.ClipboardManager;


@Component
class RemoteMsgProcessorAdapter extends MessageProcessorAdapter {

	@Autowired
	private AppStateManager appStateManager;

	@Autowired
	private ClipboardManager clipboardManager;

	@Override
	public void clipboardSync(String text) {
		clipboardManager.setClipboardContent(text);
	}

	@Override
	public void remoteExit() {
		appStateManager.exitRemote();
	}
}
