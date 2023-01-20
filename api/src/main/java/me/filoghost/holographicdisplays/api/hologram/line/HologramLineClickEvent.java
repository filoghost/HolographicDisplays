/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.bukkit.entity.Player;

/**
 * The event of a player clicking on a {@link ClickableHologramLine}.
 * <p>
 * This is not a Bukkit event, the listener must be set with
 * {@link ClickableHologramLine#setClickListener(HologramLineClickListener)}.
 *
 * @since 1
 */
public interface HologramLineClickEvent {

    /**
     * Returns the player who clicked on the line.
     *
     * @return the player who clicked
     * @since 1
     */
    Player getPlayer();

    /**
     * Returns the click type
     *
     * @return the click type
     * @since 3
     */
    HologramClickType getClickType();
}
