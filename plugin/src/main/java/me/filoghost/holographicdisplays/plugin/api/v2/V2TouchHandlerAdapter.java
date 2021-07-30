/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.hologram.ClickListener;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
class V2TouchHandlerAdapter implements TouchHandler {

    private final ClickListener v3ClickListener;

    V2TouchHandlerAdapter(ClickListener v3ClickListener) {
        this.v3ClickListener = v3ClickListener;
    }

    @Override
    public void onTouch(Player player) {
        v3ClickListener.onClick(player);
    }

    public ClickListener getV3ClickListener() {
        return v3ClickListener;
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
        return this.v3ClickListener.equals(other.v3ClickListener);
    }

    @Override
    public final int hashCode() {
        return v3ClickListener.hashCode();
    }

    @Override
    public final String toString() {
        return v3ClickListener.toString();
    }

}
