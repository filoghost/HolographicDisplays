package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;

public interface Hologram {

	/**
	 * Updates the hologram. This must be used after changing the lines.
	 * @return false if the spawn was blocked.
	 */
	public boolean update();
	
	/**
	 * Hides the hologram.
	 */
	public void hide();
	
	/**
	 * Appends a line at the end.
	 * @param text - the text to append.
	 */
	public void addLine(String text);

	/**
	 * Removes a line at the given index (0 = first line)
	 * @param the index of the line to remove.
	 */
	public void removeLine(int index);

	/**
	 * Changes a line at the given index (0 = first line).
	 * @param index - the index of the line to change.
	 * @param text - the new text of the line.
	 */
	public void setLine(int index, String text);
	
	/**
	 * Adds a line before the given index (0 = insert before the first line).
	 * @param index - the text will be inserted before this index.
	 * @param text - the text to insert.
	 */
	public void insertLine(int index, String text);

	/**
	 * @return a copy of the lines.
	 */
	public String[] getLines();
	
	/**
	 * @return the amount of lines.
	 */
	public int getLinesLength();
	
	/**
	 * Removes all the lines from the hologram.
	 */
	public void clearLines();
	
	/**
	 * @return the location of the hologram.
	 */
	public Location getLocation();
	
	/**
	 * @return the X coordinate of the hologram. 
	 */
	public double getX();
	
	/**
	 * @return the Y coordinate of the hologram. 
	 */
	public double getY();
	
	/**
	 * @return the Z coordinate of the hologram. 
	 */
	public double getZ();
	
	/**
	 * @return the world of the hologram. 
	 */
	public World getWorld();
	
	/**
	 * Change the location of the hologram. You have to call update() after this method.
	 * Please note that this method will create new entities every time, so use it wisely.
	 * @param location - the new location of the hologram.
	 */
	public void setLocation(Location location);
	
	/**
	 * Sets the touch handler of the hologram: whenever a player right clicks it, the onTouch()
	 * method of the TouchHandler is called. If null, the previous touch handler will be removed.
	 * @param handler - the new TouchHandler.
	 */
	public void setTouchHandler(TouchHandler handler);
	
	/**
	 * @return the current touch handler, null if hasTouchHandler() is false.
	 */
	public TouchHandler getTouchHandler();
	
	/**
	 * @return true if the hologram has a touch handler.
	 */
	public boolean hasTouchHandler();
	
	/**
	 * @return the timestamp of when the hologram was created, in milliseconds.
	 */
	public long getCreationTimestamp();
	
	/**
	 * Deletes this hologram, removing it from the lists.
	 */
	public void delete();
	
	/**
	 * @return true if this hologram was deleted. Calling update() on a deleted hologram will throw an exception.
	 */
	public boolean isDeleted();
	
}
