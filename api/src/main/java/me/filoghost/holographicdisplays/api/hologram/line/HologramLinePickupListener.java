/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.jetbrains.annotations.NotNull;

/**
 * The listener class for {@link HologramLinePickupEvent}.
 * <p>
 * This is not a Bukkit listener, it must be set with
 * {@link ItemHologramLine#setPickupListener(HologramLinePickupListener)}.
 *
 * @since 1
 */
@FunctionalInterface
public interface HologramLinePickupListener {

    /**
     * Invoked when a player is being in pickup range of the item line. Note that this method is invoked repeatedly
     * every tick until the player moves out of range, the listener is removed, the item line is no longer visible to
     * the player, or the item line is removed.
     *
     * @param pickupEvent the event data
     * @since 1
     */
    void onPickup(@NotNull HologramLinePickupEvent pickupEvent);

}
