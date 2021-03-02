/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms.entity;

import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;

/**
 * An interface to represent a custom NMS entity being part of a hologram.
 */
public interface NMSEntityBase {
    
    // Returns the linked line, all the entities are part of a piece. Should never be null.
    StandardHologramLine getHologramLine();

    // Sets the location through NMS.
    void setLocationNMS(double x, double y, double z);
    
    // Returns if the entity is dead through NMS.
    boolean isDeadNMS();
    
    // Kills the entity through NMS.
    void killEntityNMS();
    
    // The entity ID.
    int getIdNMS();
    
    // Returns the bukkit entity.
    org.bukkit.entity.Entity getBukkitEntityNMS();

}
