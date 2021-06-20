/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2TouchHandlerAdapter implements com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler {

    private final TouchHandler newTouchHandler;

    V2TouchHandlerAdapter(TouchHandler newTouchHandler) {
        this.newTouchHandler = newTouchHandler;
    }

    @Override
    public void onTouch(Player player) {
        newTouchHandler.onTouch(player);
    }

    public TouchHandler getNewTouchHandler() {
        return newTouchHandler;
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
        return this.newTouchHandler.equals(other.newTouchHandler);
    }

    @Override
    public final int hashCode() {
        return newTouchHandler.hashCode();
    }

    @Override
    public final String toString() {
        return newTouchHandler.toString();
    }

}
