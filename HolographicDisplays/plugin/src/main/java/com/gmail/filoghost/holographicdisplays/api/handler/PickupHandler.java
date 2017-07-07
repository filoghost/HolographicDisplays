package com.gmail.filoghost.holographicdisplays.api.handler;

import org.bukkit.entity.Player;

/**
 * Interface to handle items being picked up by players.
 */
public interface PickupHandler {

	/**
	 * Called when a player picks up the item.
	 * @param player the player who picked up the item
	 */
	public void onPickup(Player player);
	
}
