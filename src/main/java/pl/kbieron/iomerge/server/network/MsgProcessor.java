package pl.kbieron.iomerge.server.network;

import javax.inject.Inject;
import pl.kbieron.iomerge.model.MessageProcessorAdapter;
import pl.kbieron.iomerge.server.appState.AppStateManager;
import pl.kbieron.iomerge.server.utils.ClipboardManager;


class MsgProcessor extends MessageProcessorAdapter {

	@Inject
	private AppStateManager appStateManager;

	@Inject
	private ClipboardManager clipboardManager;

	@Inject
	private ConnectionHandler connectionHandler;

	@Override
	public void clipboardSync(String text) {
		clipboardManager.setClipboardContent(text);
	}

	@Override
	public void remoteExit() {
		appStateManager.exitRemote();
	}

	@Override
	public void heartbeat() {
		connectionHandler.keepAlive();
	}
}
