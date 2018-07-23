package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;

/**
 * An interface to represent a custom NMS entity being part of a hologram.
 */
public interface NMSEntityBase {

	/**
	 * Returns the linked HologramLine, all the entities are part of a piece.
	 *
	 * @return the hologram line, should never be null.
	 */
	HologramLine getHologramLine();

	/**
	 * Sets if the entity should tick or not.
	 *
	 * @param lock if the entity should tick or not.
	 */
	void setLockTick(boolean lock);

	/**
	 * Sets the entity location through NMS.
	 *
	 * @param x the new x coordinate.
	 * @param y the new y coordinate.
	 * @param z the new z coordinate.
	 */
	void setLocationNMS(double x, double y, double z);

	/**
	 * Returns if the entity is dead through NMS.
	 *
	 * @return true if the entity is dead, false otherwise.
	 */
	boolean isDeadNMS();

	/**
	 * Kills the entity through NMS.
	 */
	void killEntityNMS();

	/**
	 * Returns the entity ID through NMS.
	 *
	 * @return true if the entity is dead, false otherwise.
	 */
	int getIdNMS();

	/**
	 * Returns the bukkit Entity.
	 *
	 * @return the bukkit Entity instance.
	 */
	org.bukkit.entity.Entity getBukkitEntityNMS();
}
