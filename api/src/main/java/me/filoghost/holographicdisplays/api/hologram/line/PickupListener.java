/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to handle hologram lines being picked up by players.
 *
 * @since 1
 */
@FunctionalInterface
public interface PickupListener {

    /**
     * Called when a player picks up the item.
     *
     * @param player the player who picked up the item
     * @since 1
     */
    void onPickup(@NotNull Player player);

}
