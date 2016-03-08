package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;

/**
 * An interface to represent a custom NMS entity being part of a hologram.
 */
public interface NMSEntityBase {
	
	// Returns the linked CraftHologramLine, all the entities are part of a piece. Should never be null.
	public CraftHologramLine getHologramLine();

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
