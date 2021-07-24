/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Location;

public class InternalHologramManager extends BaseHologramManager<InternalHologram> {

    private final LineTrackerManager lineTrackerManager;

    public InternalHologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    public InternalHologram createHologram(Location source, String name) {
        InternalHologram hologram = new InternalHologram(source, name, lineTrackerManager);
        super.addHologram(hologram);
        return hologram;
    }

    public InternalHologram getHologramByName(String name) {
        for (InternalHologram hologram : getHolograms()) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram;
            }
        }
        return null;
    }

}
