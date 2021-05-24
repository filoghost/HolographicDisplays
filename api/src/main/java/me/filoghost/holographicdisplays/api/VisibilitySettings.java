/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Settings to manage the visibility of a hologram to players. Allows to set both the default visibility and the
 * visibility to a specific player.
 * <p>
 * <b>Warning</b>: changing the visibility requires ProtocolLib, otherwise methods of this class have no effect.
 *
 * @since 1
 */
public interface VisibilitySettings {

    /**
     * Returns the default visibility of the hologram. The initial value is {@link Visibility#VISIBLE}, meaning that the
     * hologram is visible to everyone.
     *
     * @return the default visibility
     * @since 1
     */
    @NotNull Visibility getDefaultVisibility();

    /**
     * Sets the default visibility. This value affects player which do not have an individual visibility set with {@link
     * #setIndividualVisibility(Player, Visibility)} and player whose individual visibility has been reset with {@link
     * #resetIndividualVisibility(Player)}.
     *
     * @param defaultVisibility the new default visibility
     * @since 1
     */
    void setDefaultVisibility(@NotNull Visibility defaultVisibility);
    
    /**
     * Sets the visibility for a specific player, overriding the default value ({@link #getDefaultVisibility()}).
     * The individual visibility value can be reverted with {@link #resetIndividualVisibility(Player)}.
     *
     * @since 1
     */
    void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility);

    /**
     * Resets the visibility for a specific player to the default value ({@link #getDefaultVisibility()}).
     *
     * @since 1
     */
    void resetIndividualVisibility(@NotNull Player player);

    /**
     * Resets the visibility for all players to the default value ({@link #getDefaultVisibility()}).
     *
     * @since 1
     */
    void resetIndividualVisibilityAll();
    
    /**
     * Checks if a hologram is visible to a player, taking into account the individual visibility for the specific
     * player and the default visibility.
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
