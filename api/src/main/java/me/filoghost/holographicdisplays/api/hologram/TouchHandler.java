/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to handle touch holograms.
 *
 * @since 1
 */
@FunctionalInterface
public interface TouchHandler {

    /**
     * Called when a player interacts with the hologram (right click).
     *
     * @param player the player who interacts
     * @since 1
     */
    void onTouch(@NotNull Player player);

}
