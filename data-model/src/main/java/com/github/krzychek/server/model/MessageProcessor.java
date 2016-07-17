package com.github.krzychek.server.model;

@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public interface MessageProcessor {

	void mousePress();

	void mouseRelease();

	void mouseMove(int x, int y);

	void mouseWheel(int move);

	void backBtnClick();

	void homeBtnClick();

	void menuBtnClick();

	void edgeSync(Edge edge);

	void remoteExit();

	void keyPress(int keyCode);

	void keyRelease(int keyCode);

	void clipboardSync(String text);

	void keyClick(int keyCode);

	void heartbeat();
}
