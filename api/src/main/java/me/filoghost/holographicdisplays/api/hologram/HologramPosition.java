/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HologramPosition {

    static @NotNull HologramPosition create(@NotNull World world, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createHologramPosition(world, x, y, z);
    }

    static @NotNull HologramPosition create(@NotNull String worldName, double x, double y, double z) {
        return HolographicDisplaysAPIProvider.getImplementation().createHologramPosition(worldName, x, y, z);
    }

    static @NotNull HologramPosition fromLocation(@NotNull Location location) {
        return HolographicDisplaysAPIProvider.getImplementation().createHologramPosition(location);
    }

    @NotNull String getWorldName();

    void setWorldName(@NotNull String worldName);

    @Nullable World getWorldIfLoaded();

    void setWorld(@NotNull World world);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    void add(double x, double y, double z);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double distance(@NotNull Location location);

    double distanceSquared(@NotNull Location location);

    @NotNull Location toLocation();

}
