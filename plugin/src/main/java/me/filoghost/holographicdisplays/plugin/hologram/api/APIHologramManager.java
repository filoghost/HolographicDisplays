/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class APIHologramManager extends BaseHologramManager<APIHologram> {

    private final LineTrackerManager lineTrackerManager;

    public APIHologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    public APIHologram createHologram(Location source, Plugin plugin) {
        APIHologram hologram = new APIHologram(source, plugin, this, lineTrackerManager);
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
