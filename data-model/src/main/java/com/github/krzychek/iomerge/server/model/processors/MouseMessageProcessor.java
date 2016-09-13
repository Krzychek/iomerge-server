package com.github.krzychek.iomerge.server.model.processors;

import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton;


public interface MouseMessageProcessor {

	void mousePress(MouseButton button);

	void mouseRelease(MouseButton button);

	void mouseMove(int x, int y);

	void mouseWheel(int rotation);
}
