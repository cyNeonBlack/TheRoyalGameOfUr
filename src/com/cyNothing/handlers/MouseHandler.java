package com.cyNothing.handlers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.cyNothing.window.Panel;

public class MouseHandler implements MouseListener {

	private Panel panel;
	
	public MouseHandler(Panel _p) {
		
		panel = _p;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		panel.click(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
