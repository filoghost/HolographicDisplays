package com.gmail.filoghost.holographicdisplays.exception;

public class TooWideException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int width;
	
	public TooWideException(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}

}
