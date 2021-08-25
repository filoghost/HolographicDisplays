/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLine;
import me.filoghost.holographicdisplays.common.Position;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Objects;

abstract class PositionBasedLineTracker<T extends BaseHologramLine> extends LineTracker<T> {

    private static final int ENTITY_VIEW_RANGE = 64;

    protected Position position;
    private boolean positionChanged;

    PositionBasedLineTracker(T line) {
        super(line);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        Position position = line.getPosition();
        if (!Objects.equals(this.position, position)) {
            this.position = position;
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

        double diffX = Math.abs(playerLocation.getX() - position.getX());
        double diffZ = Math.abs(playerLocation.getZ() - position.getZ());

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
