/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramLineAdapter;
import me.filoghost.holographicdisplays.plugin.hologram.base.EditableHologramLine;
import org.jetbrains.annotations.NotNull;

public interface APIHologramLine extends HologramLine, EditableHologramLine {

    @Override
    @NotNull APIHologram getHologram();

    void setChanged();

    V2HologramLineAdapter getV2Adapter();

}
