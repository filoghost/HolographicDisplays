/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.core.tick.TickClock;
import org.bukkit.Location;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Objects;

abstract class PositionBasedLineTracker<T extends Viewer> extends LineTracker<T> {

    protected PositionCoordinates position;
    private boolean positionChanged;

    protected PositionBasedLineTracker(TickClock tickClock) {
        super(tickClock);
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        PositionCoordinates position = getLine().getPosition();
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
    protected final boolean shouldTrackPlayer(CachedPlayer player) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getWorld() != getLine().getWorldIfLoaded()) {
            return false;
        }

        double diffX = Math.abs(playerLocation.getX() - position.getX());
        double diffZ = Math.abs(playerLocation.getZ() - position.getZ());

        return diffX <= getViewRange()
                && diffZ <= getViewRange()
                && getLine().isVisibleTo(player.getBukkitPlayer());
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendChangesPackets(Viewers<T> viewers) {
        if (positionChanged) {
            sendPositionChangePackets(viewers);
        }
    }

    protected abstract void sendPositionChangePackets(Viewers<T> viewers);

    protected abstract double getViewRange();

}
