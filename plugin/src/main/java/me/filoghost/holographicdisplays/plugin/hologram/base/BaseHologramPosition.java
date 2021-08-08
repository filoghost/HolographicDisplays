/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class BaseHologramPosition {

    private @NotNull String worldName;
    private double x, y, z;

    public BaseHologramPosition(BaseHologramPosition position) {
        Preconditions.notNull(position, "position");
        this.worldName = position.worldName;
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    public BaseHologramPosition(@NotNull String worldName, double x, double y, double z) {
        Preconditions.notNull(worldName, "worldName");
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BaseHologramPosition(Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location's world");
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public @NotNull String getWorldName() {
        return worldName;
    }

    public void setWorldName(@NotNull String worldName) {
        Preconditions.notNull(worldName, "worldName");
        this.worldName = worldName;
    }

    public boolean isInWorld(World world) {
        return world != null && isInWorld(world.getName());
    }

    public boolean isInWorld(String worldName) {
        // Use the same comparison used by Bukkit.getWorld(...)
        return worldName != null && worldName.toLowerCase(Locale.ENGLISH).equals(this.worldName.toLowerCase(Locale.ENGLISH));
    }

    public @Nullable World getWorldIfLoaded() {
        return Bukkit.getWorld(worldName);
    }

    public void setWorld(@NotNull World world) {
        Preconditions.notNull(world, "world");
        this.worldName = world.getName();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void set(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getBlockX() {
        return Location.locToBlock(x);
    }

    public int getBlockY() {
        return Location.locToBlock(y);
    }

    public int getBlockZ() {
        return Location.locToBlock(z);
    }

    public double distance(@NotNull Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    public double distanceSquared(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        return NumberConversions.square(this.x - location.getX())
                + NumberConversions.square(this.y - location.getY())
                + NumberConversions.square(this.z - location.getZ());
    }

    public @NotNull Location toLocation() {
        return new Location(getWorldIfLoaded(), x, y, z);
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
