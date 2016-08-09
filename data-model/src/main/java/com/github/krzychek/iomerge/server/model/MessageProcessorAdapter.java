package com.github.krzychek.iomerge.server.model;

public abstract class MessageProcessorAdapter implements MessageProcessor {

	@Override
	public void mousePress(int button) {}

	@Override
	public void mouseRelease(int button) {}

	@Override
	public void mouseMove(int x, int y) {}

	@Override
	public void mouseWheel(int move) {}

	@Override
	public void backBtnClick() {}

	@Override
	public void homeBtnClick() {}

	@Override
	public void menuBtnClick() {}

	@Override
	public void edgeSync(Edge edge) {}

	@Override
	public void keyClick(int keyCode) {}

	@Override
	public void heartbeat() {}

	@Override
	public void returnToLocal(float position) {}

	@Override
	public void keyPress(int keyCode) {}

	@Override
	public void keyRelease(int keyCode) {}

	@Override
	public void clipboardSync(String text) {}
}
