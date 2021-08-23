/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import me.filoghost.holographicdisplays.api.hologram.line.PickupListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class V3PickupListenerAdapter implements PickupListener {

    private final PickupHandler v2PickupHandler;

    public V3PickupListenerAdapter(PickupHandler v2PickupHandler) {
        this.v2PickupHandler = v2PickupHandler;
    }

    @Override
    public void onPickup(@NotNull Player player) {
        v2PickupHandler.onPickup(player);
    }

    public PickupHandler getV2PickupHandler() {
        return v2PickupHandler;
    }

}
