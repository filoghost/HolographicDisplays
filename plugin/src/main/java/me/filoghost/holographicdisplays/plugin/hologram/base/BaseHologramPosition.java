/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class BaseHologramPosition implements HologramPosition {

    private final @NotNull String worldName;
    private final double x, y, z;

    public BaseHologramPosition(@NotNull String worldName, double x, double y, double z) {
        Preconditions.notNull(worldName, "worldName");
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BaseHologramPosition(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location's world");
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    @Override
    public @NotNull String getWorldName() {
        return worldName;
    }

    public boolean isInWorld(@Nullable World world) {
        return world != null && isInWorld(world.getName());
    }

    public boolean isInWorld(@Nullable String worldName) {
        // Use the same comparison used by Bukkit.getWorld(...)
        return worldName != null && worldName.toLowerCase(Locale.ENGLISH).equals(this.worldName.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public @Nullable World getWorldIfLoaded() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public int getBlockX() {
        return Location.locToBlock(x);
    }

    @Override
    public int getBlockY() {
        return Location.locToBlock(y);
    }

    @Override
    public int getBlockZ() {
        return Location.locToBlock(z);
    }

    @Override
    public @NotNull BaseHologramPosition add(double x, double y, double z) {
        return new BaseHologramPosition(this.worldName, this.x + x, this.y + y, this.z + z);
    }

    public @NotNull BaseHologramPosition withX(double x) {
        return new BaseHologramPosition(this.worldName, x, this.y, this.z);
    }

    public @NotNull BaseHologramPosition withY(double y) {
        return new BaseHologramPosition(this.worldName, this.x, y, this.z);
    }

    public @NotNull BaseHologramPosition withZ(double z) {
        return new BaseHologramPosition(this.worldName, this.x, this.y, z);
    }

    @Override
    public double distance(@NotNull Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    @Override
    public double distanceSquared(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        return NumberConversions.square(this.x - location.getX())
                + NumberConversions.square(this.y - location.getY())
                + NumberConversions.square(this.z - location.getZ());
    }

    @Override
    public @NotNull Location toLocation() {
        return new Location(getWorldIfLoaded(), x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseHologramPosition other = (BaseHologramPosition) obj;
        return this.worldName.equals(other.worldName)
                && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y)
                && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + worldName.hashCode();
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        return result;
    }

    @Override
    public String toString() {
        return "HologramPosition{"
                + "worldName=" + worldName
                + ", x=" + x
                + ", y=" + y
                + ", z=" + z
                + "}";
    }

}
