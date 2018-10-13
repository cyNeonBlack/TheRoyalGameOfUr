package com.cyNothing.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
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

	public BufferedImage rCounter = null, lCounter = null, rosette = null;

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

		try {
			rosette = ImageIO.read(new File("resources/Rosette.png"));
			lCounter = ImageIO.read(new File("resources/lCounter.png"));
			rCounter = ImageIO.read(new File("resources/rCounter.png"));
		} catch (Exception e) {
			System.out.println(e);
		}

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

		if (roll[4] == 0) { // ai rolled 0.
			return;
		}

		// Finds all valid places
		validPlaces.add(new Coordinate(roll[4], 0));

		for (Counter c : board.rightCounters)
			validPlaces.add(new Coordinate(c.getPlace() + roll[4], 0));

		for (Counter c : board.rightCounters)
			for (int x = validPlaces.size() - 1; x >= 0; x--)
				if (validPlaces.get(x).getX() == c.getPlace())
					validPlaces.remove(x);

		for (Counter c : board.leftCounters)
			if (c.getPlace() == 8)
				for (Coordinate c2 : validPlaces)
					if (c2.getX() == 8) {
						validPlaces.remove(c2);
						break;
					}

		for (int x = validPlaces.size() - 1; x >= 0; x--)
			if (validPlaces.get(x).getX() > 15)
				validPlaces.remove(x);

		// Defaults all move values to 1.
		for (Coordinate c : validPlaces)
			c.setY(0);

		for (Coordinate c : validPlaces) {
			int x = c.getX();

			// Land on Rosettes.
			if (x == 8)
				c.addY(10);

			if (x == 4 || x == 14)
				c.addY(8);

			// Enter board
			if (x == roll[4])
				c.addY(1);

			// Exit board
			if (x == 15)
				c.subY(1);

			// Escape War Zone
			if (x < 13 && x > 4)
				c.subY(1);

			// Enter War Zone
			if (x - roll[4] < 5 && x > 4)
				c.subY(2);

			// Leaving 8
			if (x - roll[4] == 8)
				c.subY(5);

			for (Counter c2 : board.leftCounters) { // All enemy logic

				if (x - roll[4] > 4 && x - roll[4] < 13) {

					// Escape 2 squares from enemy
					if (c2.getPlace() + 2 == x - roll[4] && x - roll[4] != 8)
						c.addY(6);

					// Escapes 1/3 squares from enemy
					if (c2.getPlace() + 1 == x - roll[4] || c2.getPlace() + 3 == x - roll[4] && x - roll[4] != 8)
						c.addY(5);

					// Escape 4 squares from enemy
					if (c2.getPlace() + 4 == x - roll[4] && x - roll[4] != 8)
						c.addY(3);
				}

				if (x > 4) {
					// Kill enemy
					if (c2.getPlace() == x)
						c.addY(7);

					// Enter 2 squares from enemy
					if (c2.getPlace() + 2 == x && x != 8)
						c.subY(6);

					// Enter 1/3 squares from enemy
					if (c2.getPlace() + 3 == x || c2.getPlace() + 1 == x && x != 8)
						c.subY(5);

					// Enter 4 squares from enemy
					if (c2.getPlace() + 4 == x && x != 8)
						c.subY(3);
				}
			}
		} // Values given

		// Sort values so highest value becomes first element
		validPlaces.sort(new Comparator<Coordinate>() {
			@Override
			public int compare(Coordinate o1, Coordinate o2) {
				return o2.getY() - o1.getY();
			}
		});

		System.out.println("--===--");

		for (Coordinate c : validPlaces) {
			System.out.println(c.getX() + ": " + c.getY());
		}

		// If the highest value option is putting a piece on the board
		if (validPlaces.get(0).getX() == roll[4])
			click(RES / 2 + RES * 4, RES / 2 + RES * 2);

		else { // Otherwise

			int mx = board.right[validPlaces.get(0).getX() - roll[4]].getX(),
					my = board.right[validPlaces.get(0).getX() - roll[4]].getY(); // x and y

			// Change coords from grid to absolute
			mx++;
			mx *= RES;
			mx += RES / 2;
			my++;
			my *= RES;
			my += RES / 2;

			click(mx, my);
		}

		if (validPlaces.get(0).getX() == 4 || validPlaces.get(0).getX() == 8 || validPlaces.get(0).getX() == 14) {
			ai(); // If the bot landed on a rosette, go again
		}
	}

	@Override
	public void paintComponent(Graphics g) { // handles rendering

		g.clearRect(0, 0, width, height); // Clears everything
		g.setColor(new Color(0, 96, 0)); // Green
		g.fillRect(0, 0, width, height); // Fills background

		// Draws Roll
		for (int x = 0; x < 4; x++) {
			Color tmp = roll[x] == 0 ? new Color(0, 0, 0) : new Color(255, 255, 255);
			g.setColor(tmp);
			g.fillOval(x * RES + RES * 3 / 4, height - RES * 3 / 4, RES / 2, RES / 2);
		}

		// Draws pieces on left side
		for (int x = 0; x < lCountersLeft; x++) {
			g.drawImage(lCounter, 0, x * RES + RES, null);
		}

		// Draws pieces on right side
		for (int x = 0; x < rCountersLeft; x++) {
			g.drawImage(rCounter, RES * 4, x * RES + RES, null);
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
