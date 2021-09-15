/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class ImmutablePosition implements Position {

    private final @NotNull String worldName;
    private final double x, y, z;

    public ImmutablePosition(@NotNull String worldName, double x, double y, double z) {
        Preconditions.notNull(worldName, "worldName");
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static ImmutablePosition of(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location.getWorld()");
        return new ImmutablePosition(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    public static ImmutablePosition of(@NotNull Position position) {
        Preconditions.notNull(position, "position");
        if (position instanceof ImmutablePosition) {
            return (ImmutablePosition) position;
        } else {
            return new ImmutablePosition(position.getWorldName(), position.getX(), position.getY(), position.getZ());
        }
    }

    @Override
    public @NotNull String getWorldName() {
        return worldName;
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
    public @Nullable World getWorldIfLoaded() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public boolean isInSameWorld(@NotNull Position position) {
        Preconditions.notNull(position, "position");
        return isInWorld(position.getWorldName());
    }

    @Override
    public boolean isInSameWorld(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        return isInWorld(location.getWorld());
    }

    @Override
    public boolean isInSameWorld(@NotNull Entity entity) {
        Preconditions.notNull(entity, "entity");
        return isInWorld(entity.getWorld());
    }

    @Override
    public boolean isInWorld(@Nullable World world) {
        return world != null && isInWorld(world.getName());
    }

    @Override
    public boolean isInWorld(@Nullable String worldName) {
        // Use the same comparison used by Bukkit.getWorld(...)
        return worldName != null && worldName.toLowerCase(Locale.ENGLISH).equals(this.worldName.toLowerCase(Locale.ENGLISH));
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
    public @NotNull ImmutablePosition add(double x, double y, double z) {
        return new ImmutablePosition(this.worldName, this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull ImmutablePosition subtract(double x, double y, double z) {
        return new ImmutablePosition(this.worldName, this.x - x, this.y - y, this.z - z);
    }

    public @NotNull ImmutablePosition withX(double x) {
        return new ImmutablePosition(this.worldName, x, this.y, this.z);
    }

    public @NotNull ImmutablePosition withY(double y) {
        return new ImmutablePosition(this.worldName, this.x, y, this.z);
    }

    public @NotNull ImmutablePosition withZ(double z) {
        return new ImmutablePosition(this.worldName, this.x, this.y, z);
    }

    @Override
    public double distance(@NotNull Position position) {
        return Math.sqrt(distanceSquared(position));
    }

    @Override
    public double distance(@NotNull Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    @Override
    public double distance(@NotNull Entity entity) {
        return Math.sqrt(distanceSquared(entity));
    }

    @Override
    public double distanceSquared(@NotNull Position position) {
        Preconditions.notNull(position, "position");
        checkSameWorld(position.getWorldName());

        return NumberConversions.square(this.x - position.getX())
                + NumberConversions.square(this.y - position.getY())
                + NumberConversions.square(this.z - position.getZ());
    }

    @Override
    public double distanceSquared(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location.getWorld()");
        checkSameWorld(location.getWorld().getName());

        return NumberConversions.square(this.x - location.getX())
                + NumberConversions.square(this.y - location.getY())
                + NumberConversions.square(this.z - location.getZ());
    }

    @Override
    public double distanceSquared(@NotNull Entity entity) {
        Preconditions.notNull(entity, "entity");
        return distanceSquared(entity.getLocation());
    }

    private void checkSameWorld(String worldName) {
        Preconditions.checkArgument(
                isInWorld(worldName),
                "cannot measure distance between " + this.worldName + " and " + worldName);
    }

    @Override
    public @NotNull Location toLocation() {
        return new Location(getWorldIfLoaded(), x, y, z);
    }

    @Override
    public @NotNull Vector toVector() {
        return new Vector(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ImmutablePosition other = (ImmutablePosition) obj;
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
        return "Position{"
                + "worldName=" + worldName
                + ", x=" + x
                + ", y=" + y
                + ", z=" + z
                + "}";
    }

}
