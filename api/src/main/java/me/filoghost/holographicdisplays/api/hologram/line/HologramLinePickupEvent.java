/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.bukkit.entity.Player;

/**
 * The event of a player being in pickup range of an {@link ItemHologramLine}.
 * <p>
 * This is not a Bukkit event, the listener must be set with
 * {@link ItemHologramLine#setPickupListener(HologramLinePickupListener)}.
 *
 * @since 1
 */
public interface HologramLinePickupEvent {

    /**
     * Returns the player being in pickup range of the item line.
     *
     * @return the player in pickup range of the item line
     * @since 1
     */
    Player getPlayer();

}
