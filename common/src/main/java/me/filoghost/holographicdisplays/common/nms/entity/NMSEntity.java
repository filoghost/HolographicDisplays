/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import org.bukkit.entity.Player;

/**
 * A custom entity that is part of a hologram.
 */
public interface NMSEntity {
    
    StandardHologramLine getHologramLine();
    
    void setLocationNMS(double x, double y, double z);
    
    boolean isDeadNMS();
    
    void killEntityNMS();
    
    int getIdNMS();
    
    org.bukkit.entity.Entity getBukkitEntityNMS();
    
    boolean isTrackedBy(Player bukkitPlayer);

}
