package com.gmail.filoghost.holographicdisplays.api;

import org.bukkit.entity.Player;

/**
 * This object is used to manage the visibility of a hologram.
 * It allows to hide/show the hologram to certain players, and the default behaviour
 * (when a hologram is not specifically being hidden/shown to a player) can be customized.
 */
public interface VisibilityManager {
	
	/**
	 * Returns if the hologram is visible by default. If not changed, this value
	 * is true by default so the hologram is visible to everyone.
	 * 
	 * @return if the hologram hologram is visible by default
	 */
	public boolean isVisibleByDefault();
	
	/**
	 * Sets if the hologram is visible by default. If not changed, this value
	 * is true by default so the hologram is visible to everyone.
	 * 
	 * @param visibleByDefault the new behaviour
	 */
	public void setVisibleByDefault(boolean visibleByDefault);
	
	/**
	 * Shows the hologram to a player, overriding the value of {@link #isVisibleByDefault()}.
	 * This is persistent if the players goes offline.
	 * 
	 * @param player the involved player
	 */
	public void showTo(Player player);
	
	/**
	 * Hides the hologram to a player, overriding the value of {@link #isVisibleByDefault()}.
	 * This is persistent if the players goes offline.
	 * 
	 * @param player the involved player
	 */
	public void hideTo(Player player);
	
	/**
	 * Checks if a hologram is visible to a player.
	 * 
	 * @param player the involved player
	 * @return if the player can see the hologram
	 */
	public boolean isVisibleTo(Player player);
	
	/**
	 * Resets the visibility to the default value. If you previously called {@link #showTo(Player)}
	 * or {@link #hideTo(Player)} to override the default visibility, this method will reset it
	 * to reflect the value of {@link #isVisibleByDefault()}.
	 * 
	 * @param player the involved player
	 */
	public void resetVisibility(Player player);
	
	/**
	 * Resets the visibility for all the players. See {@link #resetVisibility(Player)} for more details.
	 */
	public void resetVisibilityAll();
	
}
