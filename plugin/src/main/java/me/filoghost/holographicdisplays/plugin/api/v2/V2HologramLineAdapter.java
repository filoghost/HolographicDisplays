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

    private final APIHologramLine v3HologramLine;

    public V2HologramLineAdapter(APIHologramLine v3HologramLine) {
        this.v3HologramLine = v3HologramLine;
    }

    @Override
    public V2HologramAdapter getParent() {
        return v3HologramLine.getParent().getV2Adapter();
    }

    @Override
    public void removeLine() {
        v3HologramLine.removeLine();
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
        return this.v3HologramLine.equals(other.v3HologramLine);
    }

    @Override
    public final int hashCode() {
        return v3HologramLine.hashCode();
    }

    @Override
    public final String toString() {
        return v3HologramLine.toString();
    }

}
