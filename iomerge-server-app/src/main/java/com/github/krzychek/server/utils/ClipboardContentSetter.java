package com.github.krzychek.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


@Component
public class ClipboardContentSetter implements ClipboardOwner {

	private final Clipboard systemClipboard;

	@Autowired
	public ClipboardContentSetter(Clipboard systemClipboard) {this.systemClipboard = systemClipboard;}


	public void setClipboardContent(String data) {
		systemClipboard.setContents(new StringSelection(data), this);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {}
}
