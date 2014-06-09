package com.gmail.filoghost.holograms.placeholders;

public class AnimatedPlaceholder extends Placeholder {

	String[] frames;
	private int index;
	
	public AnimatedPlaceholder(String longPlaceholder, int tenthsToRefresh, String[] frames) {
		super(longPlaceholder, longPlaceholder, tenthsToRefresh);
		this.frames = frames;
		update();
	}

	@Override
	public void update() {
		
		currentReplacement = frames[index];
			
		index++;
		if (index >= frames.length) {
			index = 0;
		}
	}

}
