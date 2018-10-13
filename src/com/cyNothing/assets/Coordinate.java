package com.cyNothing.assets;

public class Coordinate {

	private int x, y;

	public Coordinate(int _x, int _y) {
		setX(_x);
		setY(_y);
	}

	public Coordinate(Coordinate _c) {
		setX(_c.getX());
		setY(_c.getY());
	}

	public Coordinate() {
		setX(0);
		setY(0);
	}

	public void setX(int _x) {
		this.x = _x;
	}

	public void setY(int _y) {
		this.y = _y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void addX(int _x) {
		setX(getX() + _x);
	}

	public void subX(int _x) {
		setX(getX() - _x);
	}

	public void addY(int _y) {
		setY(getY() + _y);
	}

	public void subY(int _y) {
		setY(getY() - _y);
	}
}
