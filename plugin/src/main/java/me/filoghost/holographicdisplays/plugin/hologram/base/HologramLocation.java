/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.util.CachedBoolean;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

public class HologramLocation {

    private World world;
    private double x, y, z;
    private int chunkX, chunkZ;
    private final CachedBoolean chunkLoaded;

    public HologramLocation(Location location) {
        this.chunkLoaded = new CachedBoolean(() -> world.isChunkLoaded(chunkX, chunkZ));
        set(location);
    }

    public World getWorld() {
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

    public int getBlockX() {
        return Location.locToBlock(x);
    }

    public int getBlockY() {
        return Location.locToBlock(y);
    }

    public int getBlockZ() {
        return Location.locToBlock(z);
    }

    protected void set(Location location) {
        Preconditions.notNull(location, "location");
        set(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    protected void set(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        int newChunkX = getChunkCoord(x);
        int newChunkZ = getChunkCoord(z);
        if (this.chunkX != newChunkX || this.chunkZ != newChunkZ) {
            this.chunkX = newChunkX;
            this.chunkZ = newChunkZ;
            this.chunkLoaded.invalidate();
        }
    }

    private int getChunkCoord(double locationCoord) {
        return Location.locToBlock(locationCoord) >> 4;
    }

    public boolean isInLoadedChunk() {
        return chunkLoaded.get();
    }

    public void onChunkLoad(Chunk chunk) {
        if (world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            chunkLoaded.set(true);
        }
    }

    public void onChunkUnload(Chunk chunk) {
        if (world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            chunkLoaded.set(false);
        }
    }

    public Location toBukkitLocation() {
        return new Location(world, x, y, z);
    }

    public double distance(Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    public double distanceSquared(Location location) {
        return NumberConversions.square(this.x - location.getX())
                + NumberConversions.square(this.y - location.getY())
                + NumberConversions.square(this.z - location.getZ());
    }

    @Override
    public String toString() {
        return "HologramLocation ["
                + "world=" + world
                + ", x=" + x
                + ", y=" + y
                + ", z=" + z
                + "]";
    }

}
