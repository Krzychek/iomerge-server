package com.github.krzychek.server.model;

public abstract class MessageProcessorAdapter implements MessageProcessor {

	@Override
	public void mousePress() {}

	@Override
	public void mouseRelease() {}

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
	public void remoteExit() {}

	@Override
	public void keyPress(int keyCode) {}

	@Override
	public void keyRelease(int keyCode) {}

	@Override
	public void clipboardSync(String text) {}
}
