/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.World;

import java.util.Collection;

public interface SpawnableHologramLine extends HologramLine {

    void respawn(World world, double x, double y, double z);

    void despawn();

    double getHeight();

    void collectEntityIDs(Collection<Integer> collector);

}
