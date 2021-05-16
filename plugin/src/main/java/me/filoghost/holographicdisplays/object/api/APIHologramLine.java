/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.legacy.api.v2.V2HologramLineAdapter;

public interface APIHologramLine extends HologramLine, StandardHologramLine {

    @Override
    APIHologram getParent();
    
    @Override
    default void removeLine() {
        getParent().removeLine(this);
    }

    V2HologramLineAdapter getV2Adapter();

}
