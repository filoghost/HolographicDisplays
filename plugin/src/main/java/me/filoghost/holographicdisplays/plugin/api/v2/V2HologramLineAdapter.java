/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramLine;

@SuppressWarnings("deprecation")
public abstract class V2HologramLineAdapter implements HologramLine {

    private final APIHologramLine v3Line;

    public V2HologramLineAdapter(APIHologramLine v3Line) {
        this.v3Line = v3Line;
    }

    @Override
    public V2HologramAdapter getParent() {
        return v3Line.getHologram().getV2Adapter();
    }

    @Override
    public void removeLine() {
        v3Line.getHologram().removeLine(v3Line);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof V2HologramLineAdapter)) {
            return false;
        }

        V2HologramLineAdapter other = (V2HologramLineAdapter) obj;
        return this.v3Line.equals(other.v3Line);
    }

    @Override
    public final int hashCode() {
        return v3Line.hashCode();
    }

    @Override
    public final String toString() {
        return v3Line.toString();
    }

}
