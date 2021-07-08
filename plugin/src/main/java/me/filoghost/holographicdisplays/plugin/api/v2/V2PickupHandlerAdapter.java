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

    private final PickupHandler newPickupHandler;

    V2PickupHandlerAdapter(PickupHandler newPickupHandler) {
        this.newPickupHandler = newPickupHandler;
    }

    @Override
    public void onPickup(Player player) {
        newPickupHandler.onPickup(player);
    }

    public PickupHandler getNewPickupHandler() {
        return newPickupHandler;
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
        return this.newPickupHandler.equals(other.newPickupHandler);
    }

    @Override
    public final int hashCode() {
        return newPickupHandler.hashCode();
    }

    @Override
    public final String toString() {
        return newPickupHandler.toString();
    }

}
