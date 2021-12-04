/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.hologram;

import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Entity to manage a group of vertically aligned lines, which display floating text and items.
 * To create one see {@link HolographicDisplaysAPI}.
 *
 * @since 1
 */
public interface Hologram {

    /**
     * Returns the editable list of lines.
     *
     * @since 1
     */
    @NotNull HologramLines getLines();

    /**
     * Returns the {@link VisibilitySettings} of this hologram.
     *
     * @return the VisibilitySettings of this hologram
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
     * @param worldName the world name where the hologram should be moved
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
     * @since 1
     */
    @NotNull ResolvePlaceholders getResolvePlaceholders();

    /**
     * @since 1
     */
    void setResolvePlaceholders(@NotNull ResolvePlaceholders resolvePlaceholders);

    /**
     * Deletes this hologram. Editing or teleporting the hologram when deleted
     * will throw an exception. Lines will be automatically cleared.
     * You should remove all the references of the hologram after deletion.
     *
     * @since 1
     */
    void delete();

    /**
     * Checks if a hologram was deleted.
     *
     * @return true if this hologram was deleted
     * @since 1
     */
    boolean isDeleted();

}
