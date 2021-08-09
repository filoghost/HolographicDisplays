/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import org.bukkit.Location;

public class APIHologramPosition extends BaseHologramPosition implements HologramPosition {

    public APIHologramPosition(String worldName, double x, double y, double z) {
        super(worldName, x, y, z);
    }

    public APIHologramPosition(Location location) {
        super(location);
    }

    @Override
    public APIHologramPosition add(double x, double y, double z) {
        super.add(x, y, z);
        return this;
    }

}
