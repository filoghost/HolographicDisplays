/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.object.api;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.plugin.legacy.api.v2.V2HologramLineAdapter;
import org.jetbrains.annotations.NotNull;

public interface APIHologramLine extends HologramLine, StandardHologramLine {

    @Override
    @NotNull APIHologram getParent();
    
    @Override
    default void removeLine() {
        getParent().removeLine(this);
    }

    V2HologramLineAdapter getV2Adapter();

}
