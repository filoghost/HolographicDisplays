/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.object.base;

import org.bukkit.World;

import java.util.Collection;

public interface SpawnableHologramLine {

    void respawn(World world, double x, double y, double z);

    void despawn();

    double getHeight();

    void collectEntityIDs(Collection<Integer> collector);

}
