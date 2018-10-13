package com.cyNothing.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.cyNothing.window.Panel;

public class KeyHandler implements KeyListener {

	private Panel panel;

	public KeyHandler(Panel _p) {

		panel = _p;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyChar() == 'r') {
			panel.reset();
		}

		if (e.getKeyChar() == ' ') {
			panel.roll();
		}

		if (e.getKeyChar() == '\n') {
			panel.ai();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
