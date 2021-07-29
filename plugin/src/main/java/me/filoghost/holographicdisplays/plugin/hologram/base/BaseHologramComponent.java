/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHologramComponent {

    private World world;
    private double x, y, z;
    private int chunkX, chunkZ;
    private boolean deleted;

    public final @NotNull Location getLocation() {
        return new Location(world, x, y, z);
    }

    protected final void setLocation(Location location) {
        setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    @MustBeInvokedByOverriders
    protected void setLocation(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = getChunkCoord(x);
        this.chunkZ = getChunkCoord(z);
    }

    private int getChunkCoord(double locationCoord) {
        return Location.locToBlock(locationCoord) >> 4;
    }

    public final World getWorld() {
        return world;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getZ() {
        return z;
    }

    public final int getChunkX() {
        return chunkX;
    }

    public final int getChunkZ() {
        return chunkZ;
    }

    public final boolean isDeleted() {
        return deleted;
    }

    @MustBeInvokedByOverriders
    public void setDeleted() {
        deleted = true;
    }

    protected final void checkNotDeleted() {
        Preconditions.checkState(!deleted, "not usable after being deleted");
    }

    @Override
    public final boolean equals(Object obj) {
        // Use the default identity comparison: two different instances are never equal
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        // Use the default identity hash code: each instance has a different hash code
        return super.hashCode();
    }

}
