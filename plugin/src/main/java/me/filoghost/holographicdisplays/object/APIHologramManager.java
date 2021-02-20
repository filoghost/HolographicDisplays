/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class APIHologramManager extends HologramManager<APIHologram> {

    private final NMSManager nmsManager;

    public APIHologramManager(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    public Hologram createHologram(Location source, Plugin plugin) {
        APIHologram hologram = new APIHologram(source, plugin, nmsManager, this);
        super.addHologram(hologram);
        return hologram;
    }

    public Set<Hologram> getHologramsByPlugin(Plugin plugin) {
        Set<Hologram> ownedHolograms = new HashSet<>();
        
        for (APIHologram hologram : getHolograms()) {
            if (hologram.getOwner().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }
        
        return Collections.unmodifiableSet(ownedHolograms);
    }

}
