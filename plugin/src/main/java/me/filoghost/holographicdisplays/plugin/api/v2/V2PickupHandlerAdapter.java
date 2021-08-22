/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.hologram.line.PickupListener;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2PickupHandlerAdapter implements PickupHandler {

    private final PickupListener v3PickupListener;

    V2PickupHandlerAdapter(PickupListener v3PickupListener) {
        this.v3PickupListener = v3PickupListener;
    }

    @Override
    public void onPickup(Player player) {
        v3PickupListener.onPickup(player);
    }

    public PickupListener getV3PickupListener() {
        return v3PickupListener;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof V2PickupHandlerAdapter)) {
            return false;
        }

        V2PickupHandlerAdapter other = (V2PickupHandlerAdapter) obj;
        return this.v3PickupListener.equals(other.v3PickupListener);
    }

    @Override
    public final int hashCode() {
        return v3PickupListener.hashCode();
    }

    @Override
    public final String toString() {
        return v3PickupListener.toString();
    }

}
