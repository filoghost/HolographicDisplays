/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.World;

public interface StandardHologramComponent {

    World getWorld();

    double getX();

    double getY();

    double getZ();

    int getChunkX();

    int getChunkZ();

    boolean isDeleted();

    void setDeleted();

}
