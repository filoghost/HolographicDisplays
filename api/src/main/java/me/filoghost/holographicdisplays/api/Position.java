/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an immutable 3-dimensional position in a world. Differently from a {@link Location} it holds a reference
 * to a world's name instead of a {@link World} object directly, making it suitable for unloaded worlds. Another
 * difference is the lack of yaw and pitch.
 *
 * @since 1
 */
public interface Position {

    /**
     * Returns the position with the given world and coordinates.
     *
     * @param world the world
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the created position
     * @since 1
     */
    static @NotNull Position of(@NotNull World world, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(world, x, y, z);
    }

    /**
     * Returns the position with the given world and coordinates.
     *
     * @param worldName the name of the world
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the created position
     * @since 1
     */
    static @NotNull Position of(@NotNull String worldName, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(worldName, x, y, z);
    }

    /**
     * Returns the position from a Location.
     *
     * @param location the location from which to obtain the position
     * @return the created position
     * @since 1
     */
    static @NotNull Position of(@NotNull Location location) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(location);
    }

    /**
     * Returns the position of an entity.
     *
     * @param entity the entity from which to obtain the position
     * @return the created position
     * @since 1
     */
    static @NotNull Position of(@NotNull Entity entity) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(entity);
    }

    /**
     * Returns the position of a block.
     *
     * @param block the block from which to obtain the position
     * @return the created position
     * @since 1
     */
    static @NotNull Position of(@NotNull Block block) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(block);
    }

    /**
     * Returns the name of the world of this position.
     *
     * @return the world name
     * @since 1
     */
    @NotNull String getWorldName();

    /**
     * Returns the X coordinate of this position.
     *
     * @return the X coordinate
     * @since 1
     */
    double getX();

    /**
     * Returns the Y coordinate of this position.
     *
     * @return the Y coordinate
     * @since 1
     */
    double getY();

    /**
     * Returns the Z coordinate of this position.
     *
     * @return the Z coordinate
     * @since 1
     */
    double getZ();

    /**
     * Returns the loaded world of this position or null if it's not loaded.
     *
     * @return the loaded world or null if not loaded
     * @since 1
     */
    @Nullable World getWorldIfLoaded();

    /**
     * Returns if this position is in the same world of another position.
     *
     * @param position the position to compare
     * @return true if the world is the same, false otherwise
     * @since 1
     */
    boolean isInSameWorld(@NotNull Position position);

    /**
     * Returns if this position is in the same world of a Location.
     *
     * @param location the location to compare
     * @return true if the world is the same, false otherwise
     * @since 1
     */
    boolean isInSameWorld(@NotNull Location location);

    /**
     * Returns if this position is in the same world of an entity.
     *
     * @param entity the entity to compare
     * @return true if the world is the same, false otherwise
     * @since 1
     */
    boolean isInSameWorld(@NotNull Entity entity);

    /**
     * Returns if this position is in the given world.
     *
     * @param world the world to compare
     * @return true if the world matches, false otherwise
     * @since 1
     */
    boolean isInWorld(@Nullable World world);

    /**
     * Returns if this position is in the given world name.
     *
     * @param worldName the world name to compare
     * @return true if the world name matches, false otherwise
     * @since 1
     */
    boolean isInWorld(@Nullable String worldName);

    /**
     * Returns the X coordinate of the block in this position.
     *
     * @return the block's X coordinate
     * @since 1
     */
    int getBlockX();

    /**
     * Returns the Y coordinate of the block in this position.
     *
     * @return the block's Y coordinate
     * @since 1
     */
    int getBlockY();

    /**
     * Returns the Z coordinate of the block in this position.
     *
     * @return the block's Z coordinate
     * @since 1
     */
    int getBlockZ();

    /**
     * Returns a new position made by adding the given lengths to each coordinate.
     *
     * @param x the length to add to the X coordinate
     * @param y the length to add to the Y coordinate
     * @param z the length to add to the Z coordinate
     * @return the new position
     * @since 1
     */
    @NotNull Position add(double x, double y, double z);

    /**
     * Returns a new position made by subtracting the given lengths from each coordinate.
     *
     * @param x the length to subtract from the X coordinate
     * @param y the length to subtract from the Y coordinate
     * @param z the length to subtract from the Z coordinate
     * @return the new position
     * @since 1
     */
    @NotNull Position subtract(double x, double y, double z);

    /**
     * Returns the distance between this position and another position. Avoid repeatedly calling this method as it uses
     * a costly square-root function, or consider using {@link #distanceSquared(Position)} instead.
     *
     * @param position the position from which to measure the distance
     * @return the distance from the other position
     * @since 1
     */
    double distance(@NotNull Position position);

    /**
     * Returns the distance between this position and a Location. Avoid repeatedly calling this method as it uses
     * a costly square-root function, or consider using {@link #distanceSquared(Location)} instead.
     *
     * @param location the location from which to measure the distance
     * @return the distance from the Location
     * @since 1
     */
    double distance(@NotNull Location location);

    /**
     * Returns the distance between this position and an entity. Avoid repeatedly calling this method as it uses
     * a costly square-root function, or consider using {@link #distanceSquared(Entity)} instead.
     *
     * @param entity the entity from which to measure the distance
     * @return the distance from the entity
     * @since 1
     */
    double distance(@NotNull Entity entity);

    /**
     * Returns the squared distance between this position and another position.
     *
     * @param position the position from which to measure the distance
     * @return the squared distance from the other position
     * @since 1
     */
    double distanceSquared(@NotNull Position position);

    /**
     * Returns the squared distance between this position and a Location.
     *
     * @param location the location from which to measure the distance
     * @return the squared distance from the Location
     * @since 1
     */
    double distanceSquared(@NotNull Location location);

    /**
     * Returns the squared distance between this position and an entity.
     *
     * @param entity the entity from which to measure the distance
     * @return the squared distance from the entity
     * @since 1
     */
    double distanceSquared(@NotNull Entity entity);

    /**
     * Returns a new Location derived from this position. <b>Warning</b>: if the world is not loaded, the Location will
     * contain a null {@link World}, which can lead to unexpected errors.
     *
     * @return A new Location based on this position
     * @since 1
     */
    @NotNull Location toLocation();

    /**
     * Returns a new Vector derived from this position.
     *
     * @return a new Vector based on this position
     * @since 1
     */
    @NotNull Vector toVector();

}
