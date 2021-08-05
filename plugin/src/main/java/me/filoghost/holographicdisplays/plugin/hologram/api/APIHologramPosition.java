/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import org.bukkit.Location;
import org.bukkit.World;

public class APIHologramPosition extends BaseHologramPosition implements HologramPosition {

    public APIHologramPosition(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public APIHologramPosition(Location location) {
        super(location);
    }

}
