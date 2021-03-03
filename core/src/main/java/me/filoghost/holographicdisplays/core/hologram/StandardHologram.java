/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.hologram;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface StandardHologram {

    Location getLocation();

    boolean isInChunk(Chunk chunk);

    Plugin getOwnerPlugin();

    List<? extends StandardHologramLine> getLinesUnsafe();

    boolean isVisibleTo(Player player);

    String toFormattedString();

    void refresh();

    void refresh(boolean forceRespawn);

    void refresh(boolean forceRespawn, boolean isChunkLoaded);

    void despawnEntities();

    void removeLine(StandardHologramLine line);

    int size();

    boolean isDeleted();

    void setDeleted();

}
