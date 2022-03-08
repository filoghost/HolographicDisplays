/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.jetbrains.annotations.Nullable;

public class InternalHologramManager extends BaseHologramManager<InternalHologram> {

    private final LineTrackerManager lineTrackerManager;

    public InternalHologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    public InternalHologram createHologram(String name, ImmutablePosition position) {
        if (getHologramByName(name) != null) {
            throw new IllegalStateException("hologram named \"" + name + "\" already exists");
        }
        InternalHologram hologram = new InternalHologram(position, name, lineTrackerManager);
        super.addHologram(hologram);
        return hologram;
    }

    public @Nullable InternalHologram getHologramByName(String name) {
        for (InternalHologram hologram : getHolograms()) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram;
            }
        }
        return null;
    }

}
