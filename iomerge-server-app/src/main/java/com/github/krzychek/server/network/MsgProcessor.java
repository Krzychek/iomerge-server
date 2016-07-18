package com.github.krzychek.server.network;

import com.github.krzychek.server.api.appState.AppStateManager;
import com.github.krzychek.server.model.MessageProcessor;
import com.github.krzychek.server.model.MessageProcessorAdapter;
import com.github.krzychek.server.utils.ClipboardContentSetter;
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
