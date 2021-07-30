/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import me.filoghost.holographicdisplays.api.hologram.PickupHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2PickupHandlerAdapter implements com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler {

    private final PickupHandler v3PickupHandler;

    V2PickupHandlerAdapter(PickupHandler v3PickupHandler) {
        this.v3PickupHandler = v3PickupHandler;
    }

    @Override
    public void onPickup(Player player) {
        v3PickupHandler.onPickup(player);
    }

    public PickupHandler getV3PickupHandler() {
        return v3PickupHandler;
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
        return this.v3PickupHandler.equals(other.v3PickupHandler);
    }

    @Override
    public final int hashCode() {
        return v3PickupHandler.hashCode();
    }

    @Override
    public final String toString() {
        return v3PickupHandler.toString();
    }

}
