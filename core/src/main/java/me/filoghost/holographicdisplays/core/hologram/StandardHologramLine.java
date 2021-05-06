/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.hologram;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface StandardHologramLine {

    StandardHologram getHologram();

    void respawn(World world, double x, double y, double z);

    void despawn();

    double getHeight();

    void collectTrackedEntityIDs(Player player, Collection<Integer> collector);

}
