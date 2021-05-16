/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologramManager;
import me.filoghost.holographicdisplays.placeholder.PlaceholderManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class APIHologramManager extends BaseHologramManager<APIHologram> {

    private final NMSManager nmsManager;
    private final PlaceholderManager placeholderManager;

    public APIHologramManager(NMSManager nmsManager, PlaceholderManager placeholderManager) {
        this.nmsManager = nmsManager;
        this.placeholderManager = placeholderManager;
    }

    public APIHologram createHologram(Location source, Plugin plugin) {
        APIHologram hologram = new APIHologram(source, plugin, nmsManager, this, placeholderManager);
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
