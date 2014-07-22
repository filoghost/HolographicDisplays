package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface FloatingItem {

	/**
	 * Updates the floating item. With floating items this method is not really needed.
	 * @return false if the spawn was blocked.
	 */
	public boolean update();
	
	/**
	 * Hides the floating item. To show the floating item call update().
	 */
	public void hide();
	
	/**
	 * Change the current itemStack of this floating item.
	 */
	public void setItemStack(ItemStack itemstack);
	
	/**
	 * @return the current itemStack of this floating item.
	 */
	public ItemStack getItemStack();
	
	/**
	 * @return the location of the floating item.
	 */
	public Location getLocation();
	
	/**
	 * @return the X coordinate of the floating item. 
	 */
	public double getX();
	
	/**
	 * @return the Y coordinate of the floating item. 
	 */
	public double getY();
	
	/**
	 * @return the Z coordinate of the floating item. 
	 */
	public double getZ();
	
	/**
	 * @return the world of the floating item. 
	 */
	public World getWorld();
	
	/**
	 * Teleports the floating item to a new location, without creating new entities.
	 * You don't need to call update() after this.
	 * @param location - the new location of the floating item.
	 */
	public void teleport(Location location);
	
	/**
	 * Sets the touch handler of the floating item: whenever a player right clicks it, the onTouch(...)
	 * method of the TouchHandler is called. If null, the previous touch handler will be removed.
	 * @param handler - the new TouchHandler.
	 */
	public void setTouchHandler(ItemTouchHandler handler);
	
	/**
	 * @return the current touch handler, null if hasTouchHandler() is false.
	 */
	public ItemTouchHandler getTouchHandler();
	
	/**
	 * @return true if the floating item has a touch handler.
	 */
	public boolean hasTouchHandler();
	
	/**
	 * Sets the pickup handler of the floating item: if not null, when a player picks up the item
	 * the onPickup(...) method is called. If null, the previous pickup handler will be removed.
	 * This is very useful for powerups.
	 * NOTE: the floating item is NOT removed automatically and the player WON'T actually pickup the item,
	 * in fact the method is just called when the player is near to the floating item.
	 * @param handler - the new PickupHandler.
	 */
	public void setPickupHandler(PickupHandler handler);
	
	/**
	 * @return the current touch handler, null if hasPickupHandler() is false.
	 */
	public PickupHandler getPickupHandler();
	
	/**
	 * @return true if the floating item has a pickup handler.
	 */
	public boolean hasPickupHandler();
	
	
	/**
	 * @return the timestamp of when the floating item was created, in milliseconds.
	 */
	public long getCreationTimestamp();
	
	/**
	 * Deletes this floating item, removing it from the lists.
	 */
	public void delete();
	
	/**
	 * @return true if this floating item was deleted. Calling update() on a deleted floating item will throw an exception.
	 */
	public boolean isDeleted();
}
