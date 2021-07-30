/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to handle clickable hologram lines.
 *
 * @since 1
 */
@FunctionalInterface
public interface ClickListener {

    /**
     * Called when a player clicks on a hologram line.
     *
     * @param player the player who clicked
     * @since 1
     */
    void onClick(@NotNull Player player);

}
