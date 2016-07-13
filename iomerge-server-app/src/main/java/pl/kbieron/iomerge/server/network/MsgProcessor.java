package pl.kbieron.iomerge.server.network;

import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.MessageProcessorAdapter;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;
import pl.kbieron.iomerge.server.utils.ClipboardManager;


/**
 * Server implementation of {@link pl.kbieron.iomerge.model.MessageProcessor}
 */
@Component
class MsgProcessor extends MessageProcessorAdapter {

	private final AppStateManager appStateManager;
	private final ClipboardManager clipboardManager;
	private final ConnectionHandler connectionHandler;

	public MsgProcessor(ClipboardManager clipboardManager, AppStateManager appStateManager, ConnectionHandler connectionHandler) {
		this.clipboardManager = clipboardManager;
		this.appStateManager = appStateManager;
		this.connectionHandler = connectionHandler;
	}

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
