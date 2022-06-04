/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Settings to manage the visibility of a hologram to players. Allows to set a global visibility and an individual
 * visibility for specific players.
 *
 * @since 1
 */
public interface VisibilitySettings {

    /**
     * Returns the visibility of the hologram. The initial value is {@link Visibility#VISIBLE}.
     *
     * @return the visibility
     * @since 1
     */
    @NotNull Visibility getGlobalVisibility();

    /**
     * Sets the visibility of the hologram. This value only affects player which do not have an individual visibility
     * (see {@link #setIndividualVisibility(Player, Visibility)}).
     *
     * @param visibility the new visibility
     * @since 1
     */
    void setGlobalVisibility(@NotNull Visibility visibility);

    /**
     * Sets the visibility for a specific player, overriding the global value of ({@link #getGlobalVisibility()}).
     * The individual visibility value can be removed with {@link #removeIndividualVisibility(Player)}.
     *
     * @since 1
     */
    void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility);

    /**
     * Removes the individual visibility for a player. The visibility for the player would then be determined by the
     * global visibility ({@link #getGlobalVisibility()}).
     *
     * @since 1
     */
    void removeIndividualVisibility(@NotNull Player player);

    /**
     * Removes the individual visibility of all players which have one.
     *
     * @since 1
     */
    void clearIndividualVisibilities();

    /**
     * Checks if a hologram is visible to a player, taking into account both the global visibility and the individual
     * visibility of the player (if set).
     *
     * @param player the player
     * @return if the player can see the hologram
     * @since 1
     */
    boolean isVisibleTo(@NotNull Player player);


    /**
     * The available statuses for the visibility of a hologram.
     */
    enum Visibility {

        VISIBLE,
        HIDDEN

    }

}
