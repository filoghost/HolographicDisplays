/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderLineTracker;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class APIHologramManager extends BaseHologramManager<APIHologram> {

    private final NMSManager nmsManager;
    private final PlaceholderLineTracker placeholderLineTracker;

    public APIHologramManager(NMSManager nmsManager, PlaceholderLineTracker placeholderLineTracker) {
        this.nmsManager = nmsManager;
        this.placeholderLineTracker = placeholderLineTracker;
    }

    public APIHologram createHologram(Location source, Plugin plugin) {
        APIHologram hologram = new APIHologram(source, plugin, nmsManager, this, placeholderLineTracker);
        super.addHologram(hologram);
        return hologram;
    }

    public Collection<Hologram> getHologramsByPlugin(Plugin plugin) {
        List<Hologram> ownedHolograms = new ArrayList<>();
        
        for (APIHologram hologram : getHolograms()) {
            if (hologram.getCreatorPlugin().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }
        
        return Collections.unmodifiableList(ownedHolograms);
    }

}
