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

    private final APIHologramLine newHologramLine;

    public V2HologramLineAdapter(APIHologramLine newHologramLine) {
        this.newHologramLine = newHologramLine;
    }

    @Override
    public V2HologramAdapter getParent() {
        return newHologramLine.getParent().getV2Adapter();
    }

    @Override
    public void removeLine() {
        newHologramLine.removeLine();
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
        return this.newHologramLine.equals(other.newHologramLine);
    }

    @Override
    public final int hashCode() {
        return newHologramLine.hashCode();
    }

    @Override
    public final String toString() {
        return newHologramLine.toString();
    }

}
