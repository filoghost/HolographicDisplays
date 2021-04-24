/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologramManager;
import me.filoghost.holographicdisplays.placeholder.PlaceholderManager;
import org.bukkit.Location;

public class InternalHologramManager extends BaseHologramManager<InternalHologram> {
    
    private final NMSManager nmsManager;
    private final PlaceholderManager placeholderManager;

    public InternalHologramManager(NMSManager nmsManager, PlaceholderManager placeholderManager) {
        this.nmsManager = nmsManager;
        this.placeholderManager = placeholderManager;
    }

    public InternalHologram createHologram(Location source, String name) {
        InternalHologram hologram = new InternalHologram(source, name, nmsManager, placeholderManager);
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
