/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.plugin.Plugin;

public class V2HologramManager extends BaseHologramManager<V2Hologram> {

    private final LineTrackerManager lineTrackerManager;

    public V2HologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    V2Hologram createHologram(ImmutablePosition position, Plugin plugin) {
        V2Hologram hologram = new V2Hologram(position, plugin, lineTrackerManager, this);
        super.addHologram(hologram);
        return hologram;
    }

}
