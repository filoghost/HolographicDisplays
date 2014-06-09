package com.gmail.filoghost.holograms.placeholders;

import java.util.List;

// Used for animated holograms.
public class AnimationData {

	private String[] lines;
	private int speed;
	
	public AnimationData(List<String> lines, int speed) {
		this.lines = lines.toArray(new String[0]);
		this.speed = speed;
	}

	public String[] getLines() {
		return lines;
	}

	public int getSpeed() {
		return speed;
	}
	
}
