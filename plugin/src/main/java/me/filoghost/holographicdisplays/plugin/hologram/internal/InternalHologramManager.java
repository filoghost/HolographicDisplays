/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderLineTracker;
import org.bukkit.Location;

public class InternalHologramManager extends BaseHologramManager<InternalHologram> {

    private final NMSManager nmsManager;
    private final PlaceholderLineTracker placeholderLineTracker;

    public InternalHologramManager(NMSManager nmsManager, PlaceholderLineTracker placeholderLineTracker) {
        this.nmsManager = nmsManager;
        this.placeholderLineTracker = placeholderLineTracker;
    }

    public InternalHologram createHologram(Location source, String name) {
        InternalHologram hologram = new InternalHologram(source, name, nmsManager, placeholderLineTracker);
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

    public boolean isExistingHologram(String name) {
        return getHologramByName(name) != null;
    }

}
