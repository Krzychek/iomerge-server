package com.github.krzychek.iomerge.server.model;

@SuppressWarnings({"EmptyMethod", "UnusedParameters"})
public interface MessageProcessor {

	void mousePress(int button);

	void mouseRelease(int button);

	void mouseMove(int x, int y);

	void mouseWheel(int move);

	void backBtnClick();

	void homeBtnClick();

	void menuBtnClick();

	void edgeSync(Edge edge);

	void returnToLocal(float position);

	void keyPress(int keyCode);

	void keyRelease(int keyCode);

	void clipboardSync(String text);

	void keyClick(int keyCode);

	void heartbeat();
}
