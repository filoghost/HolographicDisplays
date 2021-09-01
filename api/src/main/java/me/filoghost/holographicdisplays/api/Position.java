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

public interface Position {

    static @NotNull Position of(@NotNull World world, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(world, x, y, z);
    }

    static @NotNull Position of(@NotNull String worldName, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(worldName, x, y, z);
    }

    static @NotNull Position of(@NotNull Location location) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(location);
    }

    static @NotNull Position of(@NotNull Entity entity) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(entity);
    }

    static @NotNull Position of(@NotNull Block block) {
        return HolographicDisplaysAPIProvider.getImplementation().getPosition(block);
    }

    @NotNull String getWorldName();

    double getX();

    double getY();

    double getZ();

    @Nullable World getWorldIfLoaded();

    boolean isInSameWorld(@NotNull Position position);

    boolean isInSameWorld(@NotNull Location location);

    boolean isInSameWorld(@NotNull Entity entity);

    boolean isInWorld(@Nullable World world);

    boolean isInWorld(@Nullable String worldName);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    @NotNull Position add(double x, double y, double z);

    @NotNull Position subtract(double x, double y, double z);

    double distance(@NotNull Position position);

    double distance(@NotNull Location location);

    double distance(@NotNull Entity entity);

    double distanceSquared(@NotNull Position position);

    double distanceSquared(@NotNull Location location);

    double distanceSquared(@NotNull Entity entity);

    @NotNull Location toLocation();

    @NotNull Vector toVector();

}
