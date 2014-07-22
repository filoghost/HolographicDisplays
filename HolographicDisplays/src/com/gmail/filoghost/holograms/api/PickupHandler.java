package com.gmail.filoghost.holograms.api;

import org.bukkit.entity.Player;

/**
 * Interface to handle floating items being picked up by players.
 */
public interface PickupHandler {

	/**
	 * Called when a player picks up the floating item.
	 * @param floatingItem - the involved floating item
	 * @param player - the player who interacts
	 */
	public void onPickup(FloatingItem floatingItem, Player player);
	
}
