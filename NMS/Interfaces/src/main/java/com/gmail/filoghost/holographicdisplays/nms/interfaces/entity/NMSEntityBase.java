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
package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;

/**
 * An interface to represent a custom NMS entity being part of a hologram.
 */
public interface NMSEntityBase {
	
	// Returns the linked CraftHologramLine, all the entities are part of a piece. Should never be null.
	public HologramLine getHologramLine();

	// Sets if the entity should tick or not.
	public void setLockTick(boolean lock);
	
	// Sets the location through NMS.
	public void setLocationNMS(double x, double y, double z);
	
	// Returns if the entity is dead through NMS.
	public boolean isDeadNMS();
	
	// Kills the entity through NMS.
	public void killEntityNMS();
	
	// The entity ID.
	public int getIdNMS();
	
	// Returns the bukkit entity.
	public org.bukkit.entity.Entity getBukkitEntityNMS();

}
