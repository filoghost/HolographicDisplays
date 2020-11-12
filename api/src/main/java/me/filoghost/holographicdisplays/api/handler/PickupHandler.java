/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.handler;

import org.bukkit.entity.Player;

/**
 * Interface to handle items being picked up by players.
 */
public interface PickupHandler {

    /**
     * Called when a player picks up the item.
     * @param player the player who picked up the item
     */
    void onPickup(Player player);
    
}
