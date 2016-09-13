package com.github.krzychek.iomerge.server.model;

import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton;


@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public interface MessageProcessor {

	void mousePress(MouseButton button);

	void mouseRelease(MouseButton button);

	void mouseMove(int x, int y);

	void mouseWheel(int rotation);

	void backBtnClick();

	void homeBtnClick();

	void menuBtnClick();

	void edgeSync(Edge edge);

	void returnToLocal(float position);

	void keyPress(int character);

	void keyRelease(int character);

	void clipboardSync(String text);

	void keyClick(int keyCode);

	void heartbeat();
}
