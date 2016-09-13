package com.github.krzychek.iomerge.server.model;

import com.github.krzychek.iomerge.server.model.message.mouse.MouseButton;


public abstract class MessageProcessorAdapter implements MessageProcessor {

	@Override
	public void mousePress(MouseButton button) {}

	@Override
	public void mouseRelease(MouseButton button) {}

	@Override
	public void mouseMove(int x, int y) {}

	@Override
	public void mouseWheel(int rotation) {}

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
	public void keyPress(int character) {}

	@Override
	public void keyRelease(int character) {}

	@Override
	public void clipboardSync(String text) {}
}
