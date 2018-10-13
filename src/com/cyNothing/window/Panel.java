package com.cyNothing.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.cyNothing.assets.Coordinate;
import com.cyNothing.assets.Counter;
import com.cyNothing.handlers.KeyHandler;
import com.cyNothing.handlers.MouseHandler;

public class Panel extends JPanel {

	// public variables
	public Frame frame;
	public Board board;
	public int width, height;

	public int[] roll = new int[5];
	public int lCountersLeft, rCountersLeft;

	public static final int RES = 80;

	// private variables

	private Timer t = new Timer();
	private TimerTask painter;

	private Random r = new Random();

	public Panel(Frame _f) { // constructor

		frame = _f;

		setup();
		start();
	}

	private void setup() { // sets default values

		width = RES * 5;
		height = RES * 10;

		lCountersLeft = rCountersLeft = 7;

		setLayout(null);

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);

		addMouseListener(new MouseHandler(this));
		frame.addKeyListener(new KeyHandler(this));

		board = new Board(this);
		add(board);
	}

	public void click(int _mx, int _my) { // handles mouse input

		// Ignores clicks from the board. Board handles its own clicks.
		if (_mx > RES && _mx < width - RES && _my > RES && _my < height - RES) {
			board.click(_mx, _my);
			return;
		}

		// Rolls dice if bottom is clicked.
		if (_my > height - RES && _mx > RES / 2 && _mx < width - RES / 2) {
			roll(); // if you click on the dice, roll
		}

		// Adds piece to board if off-board counter is clicked
		if (_mx < RES && _my > RES && _my < height - RES) {
			if (roll[4] != 0 && lCountersLeft > 0) {
				Counter tmp = new Counter(board.left[roll[4]], roll[4]);

				for (Counter c : board.leftCounters) {
					if (c.equals(tmp))
						return;
				}

				board.leftCounters.add(new Counter(board.left[roll[4]], roll[4]));
				lCountersLeft--;
			}
		}

		if (_mx > RES && _my > RES && _my < height - RES) {
			if (roll[4] != 0 && rCountersLeft > 0) {

				Counter tmp = new Counter(board.right[roll[4]], roll[4]);

				for (Counter c : board.rightCounters) {
					if (c.equals(tmp))
						return;
				}

				board.rightCounters.add(new Counter(board.right[roll[4]], roll[4]));
				rCountersLeft--;
			}
		}

		// roll cheater
		// /*
		if (_my < RES) {
			for (int x = 0; x < 4; x++) {
				if (roll[x] == 0) {
					roll[x] = 1;
					roll[4]++;
					break;
				}
				if (roll[4] == 4) {
					for (int y = 0; y < 4; y++) {
						roll[y] = 0;
						roll[4] = 0;
					}
					break;
				}
			}
		}
		// */
		// end of roll cheater

	}

	public void roll() {

		roll[4] = 0;

		for (int x = 0; x < 4; x++) {
			roll[x] = r.nextInt(2); // each int randomly becomes 0 or 1
			roll[4] += roll[x];
		}
	}

	public void reset() {

		lCountersLeft = rCountersLeft = 7;
		board.leftCounters.clear();
		board.rightCounters.clear();
	}

	public void ai() {

		ArrayList<Coordinate> validPlaces = new ArrayList<Coordinate>();

		roll();

		for (Counter c : board.rightCounters)
			validPlaces.add(new Coordinate(c.getPlace() + roll[4], 0));

		for (Counter c : board.rightCounters)
			for (Coordinate x : validPlaces)
				if (c.getPlace() == x.getX())
					validPlaces.remove(x);

		if (board.rightCounters.isEmpty()) { // if there's no pieces, add one
			click(RES / 2 + RES * 4, RES / 2 + RES);
			if (board.rightCounters.get(0).getPlace() == 4)
				ai();
		} else {

		}

	}

	@Override
	public void paintComponent(Graphics g) { // handles rendering

		g.clearRect(0, 0, width, height); // Clears everything
		g.setColor(new Color(5, 114, 14)); // Green
		g.fillRect(0, 0, width, height); // Fills background

		// Draws Roll
		for (int x = 0; x < 4; x++) {
			Color tmp = roll[x] == 0 ? new Color(0, 0, 0) : new Color(255, 255, 255);
			g.setColor(tmp);
			g.fillOval(x * RES + RES * 3 / 4, height - RES * 3 / 4, RES / 2, RES / 2);
		}

		// Draws pieces on left side
		g.setColor(new Color(144, 200, 206));
		for (int x = 0; x < lCountersLeft; x++) {
			g.fillOval(RES / 8, x * RES + (RES / 8 * 9), RES / 4 * 3, RES / 4 * 3);
		}

		// Draws pieces on right side
		g.setColor(new Color(49, 131, 140));
		for (int x = 0; x < rCountersLeft; x++) {
			g.fillOval(width - RES + (RES / 8), x * RES + (RES / 8 * 9), RES / 4 * 3, RES / 4 * 3);
		}

		board.render(); // Draw everything on the Board

	}

	private void start() { // starts render loop at ~30 fps

		painter = new TimerTask() {

			@Override
			public void run() {

				repaint();
			}
		};

		t.scheduleAtFixedRate(painter, 0, Math.round(1000f / 30));
	}

}
