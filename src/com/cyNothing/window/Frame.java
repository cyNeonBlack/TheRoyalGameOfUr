package com.cyNothing.window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Frame extends JFrame {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() { // Fixes problems w/ threads and paintComponent

			@Override
			public void run() {
				new Frame();
			}
		});
	}

	private Frame() {

		super("The Royal Game of Ur"); // Sets title

		setFocusable(true); // Allows frame to be focused by event listeners
		setVisible(true); // shows frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // operation to be used on close

		Panel p = new Panel(this); // creates new panel

		add(p); // adds panel to this object
		pack(); // compresses the frame to fit the panel
		setLocationRelativeTo(null); // centers the screen

	}

}
