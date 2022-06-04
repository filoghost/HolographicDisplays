/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Group of one or more vertically aligned lines which appear as floating text lines and items. To create one see
 * {@link HolographicDisplaysAPI#createHologram(Position)}.
 * <p>
 * The lines are displayed in a top to bottom order starting from the hologram position and going down.
 *
 * @since 1
 */
public interface Hologram {

    /**
     * Returns the editable lines of this hologram.
     *
     * @return the editable lines
     * @since 1
     */
    @NotNull HologramLines getLines();

    /**
     * Returns the visibility settings.
     *
     * @return the visibility settings
     * @since 1
     */
    @NotNull VisibilitySettings getVisibilitySettings();

    /**
     * Returns the current position.
     *
     * @return the current position
     * @since 1
     */
    @NotNull Position getPosition();

    /**
     * Moves the hologram to the given position.
     *
     * @param position the new position
     * @since 1
     */
    void setPosition(@NotNull Position position);

    /**
     * Moves the hologram to the given position.
     *
     * @param worldName the name of the world where the hologram should be moved
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @since 1
     */
    void setPosition(@NotNull String worldName, double x, double y, double z);

    /**
     * Moves the hologram to the given position.
     *
     * @param world the world where the hologram should be moved
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @since 1
     */
    void setPosition(@NotNull World world, double x, double y, double z);

    /**
     * Moves the hologram to the given position.
     *
     * @param location the new position
     * @since 1
     */
    void setPosition(@NotNull Location location);

    /**
     * Returns the placeholder setting.
     *
     * @return the placeholder setting
     * @since 1
     */
    @NotNull PlaceholderSetting getPlaceholderSetting();

    /**
     * Changes the placeholder setting.
     *
     * @param placeholderSetting the new placeholder setting
     * @since 1
     */
    void setPlaceholderSetting(@NotNull PlaceholderSetting placeholderSetting);

    /**
     * Deletes this hologram, clearing the lines. Editing or teleporting the hologram after deleting it throws an
     * exception. A deleted hologram should no longer be referenced.
     *
     * @since 1
     */
    void delete();

    /**
     * Returns if this hologram is deleted.
     *
     * @return true if this hologram is deleted
     * @since 1
     */
    boolean isDeleted();

}
