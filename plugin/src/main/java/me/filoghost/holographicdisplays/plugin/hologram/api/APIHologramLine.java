/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramLineAdapter;
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
