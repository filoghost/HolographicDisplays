/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseHologramManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class APIHologramManager extends BaseHologramManager<APIHologram> {

    private final NMSManager nmsManager;

    public APIHologramManager(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    public Hologram createHologram(Location source, Plugin plugin) {
        APIHologram hologram = new APIHologram(source, plugin, nmsManager, this);
        super.addHologram(hologram);
        return hologram;
    }

    public Collection<Hologram> getHologramsByPlugin(Plugin plugin) {
        List<Hologram> ownedHolograms = new LinkedList<>();
        
        for (APIHologram hologram : getHolograms()) {
            if (hologram.getOwner().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }
        
        return Collections.unmodifiableList(ownedHolograms);
    }

}
