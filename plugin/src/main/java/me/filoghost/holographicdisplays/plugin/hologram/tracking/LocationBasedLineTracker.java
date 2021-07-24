/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

abstract class LocationBasedLineTracker<T extends StandardHologramLine> extends LineTracker<T> {

    private static final int ENTITY_VIEW_RANGE = 64;

    private World world;
    protected double locationX;
    protected double locationY;
    protected double locationZ;
    private int chunkX;
    private int chunkZ;
    private boolean chunkLoaded;
    private boolean locationChanged;

    LocationBasedLineTracker(T line, NMSManager nmsManager) {
        super(line, nmsManager);
    }

    @Override
    protected final boolean isActive() {
        return chunkLoaded;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        World world = line.getWorld();
        int chunkX = line.getChunkX();
        int chunkZ = line.getChunkZ();
        if (this.world != world || this.chunkX != chunkX || this.chunkZ != chunkZ) {
            this.world = world;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.chunkLoaded = world != null && world.isChunkLoaded(chunkX, chunkZ);
            this.locationChanged = true;
        }

        double locationX = line.getX();
        double locationY = line.getY();
        double locationZ = line.getZ();
        if (this.locationX != locationX || this.locationY != locationY || this.locationZ != locationZ) {
            this.locationX = locationX;
            this.locationY = locationY;
            this.locationZ = locationZ;
            this.locationChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        this.locationChanged = false;
    }

    @Override
    protected final boolean shouldTrackPlayer(Player player) {
        Location playerLocation = player.getLocation();
        double diffX = Math.abs(playerLocation.getX() - locationX);
        double diffZ = Math.abs(playerLocation.getZ() - locationZ);

        return playerLocation.getWorld() == world
                && diffX <= (double) ENTITY_VIEW_RANGE
                && diffZ <= (double) ENTITY_VIEW_RANGE
                && line.getHologram().isVisibleTo(player);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        if (locationChanged) {
            addLocationChangePackets(packetList);
        }
    }

    protected abstract void addLocationChangePackets(NMSPacketList packetList);

    @Override
    protected final void onChunkLoad(Chunk chunk) {
        if (this.chunkX == chunk.getX() && this.chunkZ == chunk.getZ()) {
            this.chunkLoaded = true;
        }
    }

    @Override
    protected final void onChunkUnload(Chunk chunk) {
        if (this.chunkX == chunk.getX() && this.chunkZ == chunk.getZ()) {
            this.chunkLoaded = false;
        }
    }

}
