package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface Hologram {

	@Deprecated
	public boolean update();

	@Deprecated
	public void hide();

	@Deprecated
	public void addLine(String text);

	@Deprecated
	public void removeLine(int index);

	@Deprecated
	public void setLine(int index, String text);

	@Deprecated
	public void insertLine(int index, String text);

	@Deprecated
	public String[] getLines();

	@Deprecated
	public int getLinesLength();

	@Deprecated
	public void clearLines();

	@Deprecated
	public Location getLocation();

	@Deprecated
	public double getX();

	@Deprecated
	public double getY();

	@Deprecated
	public double getZ();

	@Deprecated
	public World getWorld();

	@Deprecated
	public void setLocation(Location location);

	@Deprecated
	public void teleport(Location location);

	@Deprecated
	public void setTouchHandler(TouchHandler handler);

	@Deprecated
	public TouchHandler getTouchHandler();

	@Deprecated
	public boolean hasTouchHandler();

	@Deprecated
	public long getCreationTimestamp();

	@Deprecated
	public void delete();

	@Deprecated
	public boolean isDeleted();

}
