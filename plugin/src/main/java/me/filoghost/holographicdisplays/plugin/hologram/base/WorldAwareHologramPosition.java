/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.holographicdisplays.plugin.util.CachedBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class WorldAwareHologramPosition {

    private final BaseHologramPosition basePosition;
    private @Nullable World world;
    private int chunkX, chunkZ;
    private final CachedBoolean chunkLoaded;

    WorldAwareHologramPosition(BaseHologramPosition basePosition) {
        this.basePosition = new BaseHologramPosition(basePosition);
        this.world = Bukkit.getWorld(basePosition.getWorldName());
        this.chunkX = getChunkCoordinate(basePosition.getX());
        this.chunkZ = getChunkCoordinate(basePosition.getZ());
        this.chunkLoaded = new CachedBoolean(() -> world != null && world.isChunkLoaded(chunkX, chunkZ));
    }

    final void set(String worldName, double x, double y, double z) {
        boolean worldChanged = !basePosition.isInWorld(worldName);
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);

        basePosition.set(worldName, x, y, z);

        if (worldChanged || this.chunkX != chunkX || this.chunkZ != chunkZ) {
            if (worldChanged) {
                this.world = Bukkit.getWorld(worldName);
            }
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.chunkLoaded.invalidate();
        }
    }

    private int getChunkCoordinate(double positionCoordinate) {
        return Location.locToBlock(positionCoordinate) >> 4;
    }

    void onWorldLoad(World world) {
        if (basePosition.isInWorld(world)) {
            this.world = world;
            chunkLoaded.invalidate();
        }
    }

    void onWorldUnload(World world) {
        if (basePosition.isInWorld(world)) {
            this.world = null;
            chunkLoaded.set(false);
        }
    }

    void onChunkLoad(Chunk chunk) {
        if (world != null && world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            chunkLoaded.set(true);
        }
    }

    void onChunkUnload(Chunk chunk) {
        if (world != null && world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            chunkLoaded.set(false);
        }
    }

    boolean isChunkLoaded() {
        return chunkLoaded.get();
    }

    @Nullable World getWorldIfLoaded() {
        return world;
    }

    @NotNull String getWorldName() {
        return basePosition.getWorldName();
    }

    double getX() {
        return basePosition.getX();
    }

    double getY() {
        return basePosition.getY();
    }

    double getZ() {
        return basePosition.getZ();
    }

    BaseHologramPosition toBasePosition() {
        return new BaseHologramPosition(basePosition);
    }

    @Override
    public String toString() {
        return basePosition.toString();
    }

}
