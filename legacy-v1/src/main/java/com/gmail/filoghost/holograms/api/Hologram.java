/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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
