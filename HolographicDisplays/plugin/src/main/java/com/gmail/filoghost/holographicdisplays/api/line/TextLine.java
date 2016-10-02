package com.gmail.filoghost.holographicdisplays.api.line;

public interface TextLine extends TouchableLine {

	/**
	 * Returns the current text of this TextLine.
	 * 
	 * @return the current text of this line.
	 */
	public String getText();
	
	/**
	 * Sets the text of this TextLine.
	 * 
	 * @param text the new text of this line.
	 */
	public void setText(String text);
	
}
