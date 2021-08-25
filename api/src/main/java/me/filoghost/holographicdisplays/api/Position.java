/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Position {

    static @NotNull Position create(@NotNull World world, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(world, x, y, z);
    }

    static @NotNull Position create(@NotNull String worldName, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(worldName, x, y, z);
    }

    static @NotNull Position fromLocation(@NotNull Location location) {
        return HolographicDisplaysAPIProvider.getImplementation().createPosition(location);
    }

    @NotNull String getWorldName();

    @Nullable World getWorldIfLoaded();

    double getX();

    double getY();

    double getZ();

    @NotNull Position add(double x, double y, double z);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double distance(@NotNull Location location);

    double distanceSquared(@NotNull Location location);

    @NotNull Location toLocation();

}
