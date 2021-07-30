/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import me.filoghost.holographicdisplays.api.hologram.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2TouchHandlerAdapter implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

    private final TouchHandler v3TouchHandler;

    V2TouchHandlerAdapter(TouchHandler v3TouchHandler) {
        this.v3TouchHandler = v3TouchHandler;
    }

    @Override
    public void onTouch(Player player) {
        v3TouchHandler.onTouch(player);
    }

    public TouchHandler getV3TouchHandler() {
        return v3TouchHandler;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof V2TouchHandlerAdapter)) {
            return false;
        }

        V2TouchHandlerAdapter other = (V2TouchHandlerAdapter) obj;
        return this.v3TouchHandler.equals(other.v3TouchHandler);
    }

    @Override
    public final int hashCode() {
        return v3TouchHandler.hashCode();
    }

    @Override
    public final String toString() {
        return v3TouchHandler.toString();
    }

}
