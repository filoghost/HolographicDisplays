package com.gmail.filoghost.holographicdisplays.placeholder;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

public class CyclicPlaceholderReplacer implements PlaceholderReplacer {

	String[] frames;
	private int index;
	
	public CyclicPlaceholderReplacer(String[] frames) {
		this.frames = frames;
		index = 0;
	}

	@Override
	public String update() {
		String result = frames[index];
		
		index++;
		if (index >= frames.length) {
			index = 0;
		}
		
		return result;
	}

}
