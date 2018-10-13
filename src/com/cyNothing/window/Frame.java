package com.cyNothing.window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

		System.out.println(
				"--= The Royal Game of Ur =--\n\n"
				+ "The Rules of the Game are Simple.\n"
				+ "1. Each player rolls the dice to determine who goes first.\n"
				+ "2. Players take turns rolling the dice, and moving their counters around the board.\n"
				+ "3. Landing on a rosette allows the player to roll twice.\n"
				+ "4. Players can knock their opponents piece off the board by landing on it.\n"
				+ "5. The center rosette is considered a safe spot, and pieces can't be knocked off it.\n"
				+ "6. It takes exactly 15 steps to move off the board. No more, no less.\n"
				+ "7. The game concludes when one player gets all 7 counters to the end.\n"
				);
		
	}

}
