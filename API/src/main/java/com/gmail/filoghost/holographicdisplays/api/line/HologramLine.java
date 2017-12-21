package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

/**
 * Interface to represent a line in a Hologram.
 */
public interface HologramLine {
	
	/**
	 * Returns the parent Hologram of this line.
	 * 
	 * @return the parent Hologram.
	 */
	public Hologram getParent();
	
	/**
	 * Removes this line from the parent Hologram. Since: v2.0.1
	 * Do not call if the Hologram has been deleted, an exception will be thrown.
	 */
	public void removeLine();

}
