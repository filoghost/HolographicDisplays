/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

public class BaseHologramPosition {

    private World world;
    private double x, y, z;

    public BaseHologramPosition(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BaseHologramPosition(Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location's world");
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public @NotNull World getWorld() {
        return world;
    }

    public void setWorld(@NotNull World world) {
        Preconditions.notNull(world, "world");
        this.world = world;
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
        return new Location(world, x, y, z);
    }

    @Override
    public String toString() {
        return "HologramPosition ["
                + "world=" + world
                + ", x=" + x
                + ", y=" + y
                + ", z=" + z
                + "]";
    }

}
