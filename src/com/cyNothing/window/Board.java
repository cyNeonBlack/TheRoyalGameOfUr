package com.cyNothing.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.cyNothing.assets.Coordinate;
import com.cyNothing.assets.Counter;

public class Board extends JPanel {

	private Panel panel;
	private final int RES;

	private BufferedImage rosette = null;
	
	public int width, height;

	public Coordinate[] left, right;
	public ArrayList<Counter> leftCounters = new ArrayList<Counter>(), rightCounters = new ArrayList<Counter>();


	public Board(Panel _p) {

		panel = _p;

		RES = Panel.RES;

		setup();
	}

	private void setup() {

		width = RES * 3 + 1;
		height = RES * 8 + 1;

		setBounds((panel.width / 2 - width / 2), (panel.height / 2 - height / 2), width, height);

		left = new Coordinate[] { new Coordinate(-1, -1), new Coordinate(0, 3), new Coordinate(0, 2),
				new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1),
				new Coordinate(1, 2), new Coordinate(1, 3), new Coordinate(1, 4), new Coordinate(1, 5),
				new Coordinate(1, 6), new Coordinate(1, 7), new Coordinate(0, 7), new Coordinate(0, 6), };

		right = new Coordinate[] { new Coordinate(-1, -1), new Coordinate(2, 3), new Coordinate(2, 2),
				new Coordinate(2, 1), new Coordinate(2, 0), new Coordinate(1, 0), new Coordinate(1, 1),
				new Coordinate(1, 2), new Coordinate(1, 3), new Coordinate(1, 4), new Coordinate(1, 5),
				new Coordinate(1, 6), new Coordinate(1, 7), new Coordinate(2, 7), new Coordinate(2, 6), };
		
		try {
			rosette = ImageIO.read(new File("resources/Rosette.png"));
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void click(int _mx, int _my) {

		int mx = _mx / RES - 1; // converts absolute mouse X / Y to grid coordinates
		int my = _my / RES - 1;

		for (Counter c : leftCounters) {
			if (c.equals(new Coordinate(mx, my))) {

				int rollTotal = panel.roll[4];

				if (c.getPlace() + rollTotal > 15) {
					return;

				} else if (c.getPlace() + rollTotal == 15) {
					leftCounters.remove(c);
					return;

				} else {

					int newPlace = c.getPlace() + rollTotal;
					Coordinate tmpCoord = new Coordinate(left[newPlace]);
					Counter tmpCounter = new Counter(tmpCoord, newPlace);

					// Stops piece from going over a piece that's already there
					for (Counter lc : leftCounters) {
						if (tmpCounter.equals(lc))
							return;
					}

					// Stops piece from overwriting rc if its on square 8. otherwise, overwrites.
					for (Counter rc : rightCounters) {
						if (tmpCounter.equals(rc)) {
							if (tmpCounter.getPlace() == 8) {
								return;
							} else {
								panel.rCountersLeft++;
								rightCounters.remove(rc);
								break;
							}
						}
					}

					// Moves piece if it gets to this point
					c.set(tmpCounter);
					return;
				}
			}
		}

		for (Counter c : rightCounters) {
			if (c.equals(new Coordinate(mx, my))) {

				int rollTotal = panel.roll[4];

				if (c.getPlace() + rollTotal > 15)
					return;

				else if (c.getPlace() + rollTotal == 15) {
					rightCounters.remove(c);
					return;

				} else {

					int newPlace = c.getPlace() + rollTotal;
					Coordinate tmpCoord = new Coordinate(right[newPlace]);
					Counter tmpCounter = new Counter(tmpCoord, newPlace);

					for (Counter rc : rightCounters) {
						if (tmpCounter.equals(rc))
							return;
					}
					
					for (Counter lc : leftCounters) {
						if (tmpCounter.equals(lc)) {
							
							if (tmpCounter.getPlace() == 8) {
								return;
							} else {
								panel.lCountersLeft++;
								leftCounters.remove(lc);
								break;
							}
						}
					}

					c.set(tmpCounter);
					return;
				}
			}
		}

	}

	public void paintComponent(Graphics g) {

		Color leftPieceCol = new Color(144, 200, 206), rightPieceCol = new Color(49, 131, 140);

		// Clears board and sets background
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(150, 109, 7));
		g.fillRect(0, 0, width, height);

		// Draws grid over the board
		g.setColor(new Color(48, 48, 48));
		for (int x = 0; x < width / RES; x++) {
			for (int y = 0; y < height / RES; y++) {
				g.drawRect(x * RES, y * RES, RES, RES);
//				g.drawRect(x * 80 + 1, y * 80 + 1, RES - 2, RES - 2);
			}
		}

		// Clears the 4 squares that aren't part of the board
		g.setColor(new Color(5, 114, 14));
		g.fillRect(RES * 0, RES * 4 + 1, RES, RES * 2 - 1);
		g.fillRect(RES * 2 + 1, RES * 4 + 1, RES, RES * 2 - 1);

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 8; y++) {
				if (x % 2 == 0) {
					if (y % 6 ==0)
						g.drawImage(rosette, x * RES, y * RES, RES, RES, null);
				} else if (y == 3)
					g.drawImage(rosette, x * RES, y * RES, RES, RES, null);
			}
		}
		
		// Draw the counters
		g.setColor(leftPieceCol);
		for (Coordinate c : leftCounters)
			g.fillOval(c.getX() * RES + RES / 8, c.getY() * RES + RES / 8, RES / 4 * 3, RES / 4 * 3);
		g.setColor(rightPieceCol);
		for (Coordinate c : rightCounters)
			g.fillOval(c.getX() * RES + RES / 8, c.getY() * RES + RES / 8, RES / 4 * 3, RES / 4 * 3);
		
	}

	public void render() {

		repaint();
	}
}
