package com.gmail.filoghost.holograms.api;

import org.bukkit.entity.Player;

/**
 * Interface to handle touch floating items.
 * NOTE: this doesn't handle item pickup, just right click.
 */
public interface ItemTouchHandler {

	/**
	 * Called when a player interacts with a floating item.
	 * @param floatingItem - the involved floating item
	 * @param player - the player who interacts
	 */
	public void onTouch(FloatingItem floatingItem, Player player);
	
}
