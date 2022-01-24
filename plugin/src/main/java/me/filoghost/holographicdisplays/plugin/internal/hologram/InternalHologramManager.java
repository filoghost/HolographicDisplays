/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;

public class InternalHologramManager extends BaseHologramManager<InternalHologram> {

    private final LineTrackerManager lineTrackerManager;

    public InternalHologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    public InternalHologram createHologram(ImmutablePosition position, String name) {
        return createHologram(position, name, -1);
    }

    public InternalHologram createHologram(ImmutablePosition position, String name, int viewRange) {
        InternalHologram hologram = new InternalHologram(position, name, lineTrackerManager, viewRange);
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
