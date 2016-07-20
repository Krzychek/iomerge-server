package com.github.krzychek.iomerge.server.network;

import com.github.krzychek.iomerge.server.api.appState.AppStateManager;
import com.github.krzychek.iomerge.server.model.MessageProcessor;
import com.github.krzychek.iomerge.server.model.MessageProcessorAdapter;
import com.github.krzychek.iomerge.server.utils.ClipboardContentSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Server implementation of {@link MessageProcessor}
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
	public void returnToLocal(float position) {
		appStateManager.returnToLocal(position);
	}
}
