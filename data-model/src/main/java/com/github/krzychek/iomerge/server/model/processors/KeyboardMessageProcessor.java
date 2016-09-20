package com.github.krzychek.iomerge.server.model.processors;

import com.github.krzychek.iomerge.server.model.SpecialKey;


public interface KeyboardMessageProcessor {

	void keyPress(int character);

	void keyRelease(int character);

	void keyClick(int character);

	void stringTyped(String string);

	void specialKeyClick(SpecialKey specialKey);
}
