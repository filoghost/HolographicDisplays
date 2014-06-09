package com.gmail.filoghost.holograms.placeholders;

public abstract class Placeholder {
	
	private String longPlaceholder;
	private String shortPlaceholder;
	
	// How many tenths of second between each refresh
	private int tenthsToRefresh;
	
	// To avoid exceptions, just use the long placeholder as default;
	protected String currentReplacement;
	
	public Placeholder(String longPlaceholder, String shortPlaceholder, int tenthsToRefresh) {
		this.longPlaceholder = longPlaceholder;
		this.shortPlaceholder = shortPlaceholder;
		this.tenthsToRefresh = tenthsToRefresh;
		currentReplacement = longPlaceholder;
	}
	
	public int getTenthsToRefresh() {
		return tenthsToRefresh;
	}
	
	public String getLongPlaceholder() {
		return longPlaceholder;
	}
	
	public String getShortPlaceholder() {
		return shortPlaceholder;
	}
	
	public abstract void update();

	public CharSequence getReplacement() {
		return currentReplacement;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof Placeholder) {
			return ((Placeholder) obj).longPlaceholder.equals(this.longPlaceholder);
		}
		
		return false;
	}
}
