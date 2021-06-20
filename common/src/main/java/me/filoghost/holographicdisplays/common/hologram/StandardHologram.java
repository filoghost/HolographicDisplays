/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface StandardHologram {
    
    World getWorld();
    
    double getX();
    
    double getY();
    
    double getZ();

    boolean isInChunk(Chunk chunk);

    List<? extends StandardHologramLine> getLines();

    int getLineCount();

    Plugin getCreatorPlugin();

    boolean isVisibleTo(Player player);

    void refresh();

    void refresh(boolean forceRespawn);

    void refresh(boolean forceRespawn, boolean isChunkLoaded);

    void despawnEntities();

    boolean isDeleted();

    void setDeleted();

    String toFormattedString();

}
