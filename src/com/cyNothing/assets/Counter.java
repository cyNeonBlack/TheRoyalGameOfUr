package com.cyNothing.assets;

public class Counter extends Coordinate {

	private int place;
	
	public Counter(int x, int y, int _place) {
		
		super(x, y);
		setPlace(_place);
	}
	
	public Counter() {
		
		super();
		setPlace(0);
	}
	
	public Counter(Coordinate c, int _place) {
		
		super(c);
		setPlace(_place);
	}

	public Counter(Counter _c) {
		
		super(_c.getX(), _c.getY());
		setPlace(_c.getPlace());
	}
	
	public void set(int _x, int _y, int _p) {
		
		setX(_x);
		setY(_y);
		setPlace(_p);
	}
	
	public void set(Coordinate _c, int _p) {
		
		setX(_c.getX());
		setY(_c.getY());
		setPlace(_p);
	}
	
	public void set(Counter _c) {
		
		setX(_c.getX());
		setY(_c.getY());
		setPlace(_c.getPlace());
	}

	public boolean equals(Counter _c) {

		return (this.getPlace() == _c.getPlace()
				&& this.getX() == _c.getX()
				&& this.getY() == _c.getY());
	}
	
	public boolean equals(Coordinate _c) {
		
		return (this.getX() == _c.getX() 
				&& this.getY() == _c.getY());
	}
	
	public void setPlace(int _place) {
		
		this.place = _place;
	}

	public int getPlace() {
		
		return this.place;
	}
	
}
