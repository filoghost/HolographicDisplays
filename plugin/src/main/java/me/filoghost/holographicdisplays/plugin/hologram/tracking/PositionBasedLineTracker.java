/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

abstract class PositionBasedLineTracker<T extends BaseHologramLine> extends LineTracker<T> {

    private static final int ENTITY_VIEW_RANGE = 64;

    protected double positionX;
    protected double positionY;
    protected double positionZ;
    private boolean positionChanged;

    PositionBasedLineTracker(T line) {
        super(line);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        double positionX = line.getX();
        double positionY = line.getY();
        double positionZ = line.getZ();
        if (this.positionX != positionX || this.positionY != positionY || this.positionZ != positionZ) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.positionZ = positionZ;
            this.positionChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        this.positionChanged = false;
    }

    @Override
    protected final boolean shouldTrackPlayer(Player player) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getWorld() != line.getWorldIfLoaded()) {
            return false;
        }

        double diffX = Math.abs(playerLocation.getX() - positionX);
        double diffZ = Math.abs(playerLocation.getZ() - positionZ);

        return diffX <= (double) ENTITY_VIEW_RANGE
                && diffZ <= (double) ENTITY_VIEW_RANGE
                && line.isVisibleTo(player);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        if (positionChanged) {
            addPositionChangePackets(packetList);
        }
    }

    protected abstract void addPositionChangePackets(NMSPacketList packetList);

}
