package com.gmail.filoghost.holograms.api;

import org.bukkit.entity.Player;

/**
 * Interface to handle touch holograms.
 */
public interface TouchHandler {

	/**
	 * Called when a player interacts with the hologram.
	 * @param hologram - the involved hologram
	 * @param player - the player who interacts
	 */
	public void onTouch(Hologram hologram, Player player);
	
}
