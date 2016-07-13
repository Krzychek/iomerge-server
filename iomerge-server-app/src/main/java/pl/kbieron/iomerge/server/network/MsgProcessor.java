package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.model.MessageProcessorAdapter;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;
import pl.kbieron.iomerge.server.utils.ClipboardContentSetter;


/**
 * Server implementation of {@link pl.kbieron.iomerge.model.MessageProcessor}
 */
@Component
class MsgProcessor extends MessageProcessorAdapter {

	private final AppStateManager appStateManager;
	private final ClipboardContentSetter clipboardContentSetter;

	@Autowired
	public MsgProcessor(ClipboardContentSetter clipboardContentSetter, AppStateManager appStateManager) {
		this.clipboardContentSetter = clipboardContentSetter;
		this.appStateManager = appStateManager;
	}

	@Override
	public void clipboardSync(String text) {
		clipboardContentSetter.setClipboardContent(text);
	}

	@Override
	public void remoteExit() {
		appStateManager.exitRemote();
	}
}
