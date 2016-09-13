package com.github.krzychek.iomerge.server.model.processors;

public interface KeyboardMessageProcessor {

	void keyPress(int character);

	void keyRelease(int character);

	void keyClick(int keyCode);
}
