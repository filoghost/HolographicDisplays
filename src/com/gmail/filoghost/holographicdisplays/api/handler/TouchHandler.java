package com.gmail.filoghost.holographicdisplays.api.handler;

import org.bukkit.entity.Player;

/**
 * Interface to handle touch holograms.
 */
public interface TouchHandler {

	/**
	 * Called when a player interacts with the hologram (right click).
	 * @param player the player who interacts
	 */
	public void onTouch(Player player);
	
}
