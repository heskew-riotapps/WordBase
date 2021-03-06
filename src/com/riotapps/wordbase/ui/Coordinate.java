package com.riotapps.wordbase.ui;

public class Coordinate{
	  
	 private int xLocation = 0;
	 private int yLocation = 0;
	 private long timestamp;
	 
	 public Coordinate(){}
	 
	 public Coordinate(int xLocation, int yLocation, long timestamp){
		 this.xLocation = xLocation;
		 this.yLocation = yLocation;
		 this.timestamp = timestamp;
	 }

	public int getxLocation() {
		return xLocation;
	}

	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}