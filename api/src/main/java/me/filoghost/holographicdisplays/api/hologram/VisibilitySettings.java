/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Settings to manage the visibility of a hologram to players. Allows to set both a global visibility and an individual
 * visibility for specific players.
 *
 * @since 1
 */
public interface VisibilitySettings {

    /**
     * Returns the global visibility. This value only affects player which do not have an individual visibility (see
     * {@link #setIndividualVisibility(Player, Visibility)}).
     * <p>
     * The initial value is {@link Visibility#VISIBLE}, which means that all players can see the hologram.
     *
     * @return the visibility
     * @since 1
     */
    @NotNull Visibility getGlobalVisibility();

    /**
     * Sets the global visibility. This value only affects player which do not have an individual visibility (see
     * {@link #setIndividualVisibility(Player, Visibility)}).
     *
     * @param visibility the new visibility
     * @since 1
     */
    void setGlobalVisibility(@NotNull Visibility visibility);

    /**
     * Sets the visibility for a specific player, with precedence over the global visibility
     * ({@link #getGlobalVisibility()}). The individual visibility value can be removed with
     * {@link #removeIndividualVisibility(Player)}.
     *
     * @since 1
     */
    void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility);

    /**
     * Removes the individual visibility for a player. The visibility for the player will then be determined by the
     * global visibility ({@link #getGlobalVisibility()}).
     *
     * @since 1
     */
    void removeIndividualVisibility(@NotNull Player player);

    /**
     * Removes the individual visibility for all players. The visibility for all players will then be determined by the
     * global visibility ({@link #getGlobalVisibility()}).
     *
     * @since 1
     */
    void clearIndividualVisibilities();

    /**
     * Checks if a hologram is visible to a player, taking into account both the global visibility and the individual
     * visibility for the player (if set).
     *
     * @param player the player
     * @return if the player can see the hologram
     * @since 1
     */
    boolean isVisibleTo(@NotNull Player player);


    /**
     * The visibility status of a hologram, which can be set both globally and for individual players.
     *
     * @since 1
     */
    enum Visibility {

        /**
         * The hologram is visible.
         *
         * @since 1
         */
        VISIBLE,

        /**
         * The hologram is not visible.
         *
         * @since 1
         */
        HIDDEN

    }

}
