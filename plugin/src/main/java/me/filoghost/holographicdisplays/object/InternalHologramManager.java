/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import org.bukkit.Location;

public class InternalHologramManager extends HologramManager<InternalHologram> {
    
    private final NMSManager nmsManager;

    public InternalHologramManager(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    public InternalHologram createHologram(Location source, String name) {
        InternalHologram hologram = new InternalHologram(source, name, nmsManager, this);
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
