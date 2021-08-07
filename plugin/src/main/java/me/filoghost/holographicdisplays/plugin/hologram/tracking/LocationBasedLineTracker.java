/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

abstract class LocationBasedLineTracker<T extends BaseHologramLine> extends LineTracker<T> {

    private static final int ENTITY_VIEW_RANGE = 64;

    protected double locationX;
    protected double locationY;
    protected double locationZ;
    private boolean locationChanged;

    LocationBasedLineTracker(T line, NMSManager nmsManager) {
        super(line, nmsManager);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
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
        if (playerLocation.getWorld() != line.getWorldIfLoaded()) {
            return false;
        }

        double diffX = Math.abs(playerLocation.getX() - locationX);
        double diffZ = Math.abs(playerLocation.getZ() - locationZ);

        return diffX <= (double) ENTITY_VIEW_RANGE
                && diffZ <= (double) ENTITY_VIEW_RANGE
                && line.isVisibleTo(player);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        if (locationChanged) {
            addLocationChangePackets(packetList);
        }
    }

    protected abstract void addLocationChangePackets(NMSPacketList packetList);

}
