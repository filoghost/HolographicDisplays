/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHologramComponent {

    private World world;
    private double x, y, z;
    private int chunkX, chunkZ;

    public boolean isInChunk(Chunk chunk) {
        return world != null
                && chunk.getWorld() == world
                && chunk.getX() == chunkX
                && chunk.getZ() == chunkZ;
    }

    public boolean isInLoadedChunk() {
        return world.isChunkLoaded(chunkX, chunkZ);
    }

    public @NotNull Location getLocation() {
        return new Location(world, x, y, z);
    }

    protected void setLocation(Location location) {
        setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    protected void setLocation(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = floor(x) >> 4;
        this.chunkZ = floor(z) >> 4;
    }

    private static int floor(double num) {
        int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public @NotNull World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
