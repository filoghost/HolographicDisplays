/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This object is used to manage the visibility of a hologram.
 * It allows to hide/show the hologram to certain players, and the default behaviour
 * (when a hologram is not specifically being hidden/shown to a player) can be customized.
 * 
 * @since 1
 */
public interface VisibilityManager {
    
    /**
     * Returns if the hologram is visible by default. If not changed, this value
     * is true by default so the hologram is visible to everyone.
     * 
     * @return if the hologram hologram is visible by default
     * @since 1
     */
    boolean isVisibleByDefault();
    
    /**
     * Sets if the hologram is visible by default. If not changed, this value
     * is true by default so the hologram is visible to everyone.
     * 
     * @param visibleByDefault the new behaviour
     * @since 1
     */
    void setVisibleByDefault(boolean visibleByDefault);
    
    /**
     * Shows the hologram to a player, overriding the value of {@link #isVisibleByDefault()}.
     * This is persistent if the players goes offline.
     * 
     * @param player the involved player
     * @since 1
     */
    void showTo(@NotNull Player player);
    
    /**
     * Hides the hologram to a player, overriding the value of {@link #isVisibleByDefault()}.
     * This is persistent if the players goes offline.
     * 
     * @param player the involved player
     * @since 1
     */
    void hideTo(@NotNull Player player);
    
    /**
     * Checks if a hologram is visible to a player.
     * 
     * @param player the involved player
     * @return if the player can see the hologram
     * @since 1
     */
    boolean isVisibleTo(@NotNull Player player);
    
    /**
     * Resets the visibility to the default value. If you previously called {@link #showTo(Player)}
     * or {@link #hideTo(Player)} to override the default visibility, this method will reset it
     * to reflect the value of {@link #isVisibleByDefault()}.
     * 
     * @param player the involved player
     * @since 1
     */
    void resetVisibility(@NotNull Player player);
    
    /**
     * Resets the visibility for all the players. See {@link #resetVisibility(Player)} for more details.
     * 
     * @since 1
     */
    void resetVisibilityAll();
    
}
