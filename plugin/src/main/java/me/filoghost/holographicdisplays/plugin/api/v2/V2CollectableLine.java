/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.CollectableLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.PickupCallbackProvider;
import org.bukkit.entity.Player;

interface V2CollectableLine extends CollectableLine, V2HologramLine, PickupCallbackProvider {

    @Override
    default boolean hasPickupCallback() {
        return getPickupHandler() != null;
    }

    @Override
    default void invokePickupCallback(Player player) {
        try {
            PickupHandler pickupListener = getPickupHandler();
            if (pickupListener != null) {
                pickupListener.onPickup(player);
            }
        } catch (Throwable t) {
            logPickupCallbackException(getCreatorPlugin(), player, t);
        }
    }

}
