/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.handler;

import org.bukkit.entity.Player;

/**
 * Interface to handle touch holograms.
 */
public interface TouchHandler {

    /**
     * Called when a player interacts with the hologram (right click).
     * @param player the player who interacts
     */
    public void onTouch(Player player);
    
}
