/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.api.hologram.line.HologramClickType;
import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.base.BaseClickableHologramLine;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class ClickableLineTracker<T extends Viewer> extends LineTracker<T> {

    private final ClickableNMSPacketEntity clickableEntity;
    private final double positionOffsetY;
    private final LineClickListener lineClickListener;

    private boolean spawnClickableEntity;
    private boolean spawnClickableEntityChanged;

    public ClickableLineTracker(BaseClickableHologramLine line, NMSManager nmsManager, LineClickListener lineClickListener) {
        this.clickableEntity = nmsManager.newClickablePacketEntity();
        this.positionOffsetY = (line.getHeight() - ClickableNMSPacketEntity.SLIME_HEIGHT) / 2;
        this.lineClickListener = lineClickListener;
    }

    public void onClientClick(Player player, HologramClickType clickType) {
        if (getLine().hasClickCallback() && canInteract(player) && isInClickRange(player)) {
            getLine().onClick(player, clickType);
        }
    }

    private boolean isInClickRange(Player player) {
        Location playerLocation = player.getLocation();
        PositionCoordinates positionCoordinates = getLine().getCoordinates();

        double xDiff = playerLocation.getX() - positionCoordinates.getX();
        double yDiff = playerLocation.getY() + 1.3 - positionCoordinates.getY(); // Use shoulder height
        double zDiff = playerLocation.getZ() - positionCoordinates.getZ();

        double distanceSquared = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff);
        return distanceSquared < 5 * 5;
    }

    @Override
    public abstract BaseClickableHologramLine getLine();

    @MustBeInvokedByOverriders
    @Override
    public void onRemoval() {
        super.onRemoval();
        lineClickListener.removeLineTracker(clickableEntity.getID());
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        super.detectChanges();

        boolean spawnClickableEntity = getLine().hasClickCallback();
        if (this.spawnClickableEntity != spawnClickableEntity) {
            this.spawnClickableEntity = spawnClickableEntity;
            this.spawnClickableEntityChanged = true;
            if (spawnClickableEntity) {
                lineClickListener.addLineTracker(clickableEntity.getID(), this);
            } else {
                lineClickListener.removeLineTracker(clickableEntity.getID());
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        super.clearDetectedChanges();
        this.spawnClickableEntityChanged = false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendSpawnPackets(Viewers<T> viewers) {
        if (spawnClickableEntity) {
            viewers.sendPackets(clickableEntity.newSpawnPackets(getClickableEntityPosition()));
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendDestroyPackets(Viewers<T> viewers) {
        if (spawnClickableEntity) {
            viewers.sendPackets(clickableEntity.newDestroyPackets());
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendChangesPackets(Viewers<T> viewers) {
        super.sendChangesPackets(viewers);

        if (spawnClickableEntityChanged) {
            if (spawnClickableEntity) {
                viewers.sendPackets(clickableEntity.newSpawnPackets(getClickableEntityPosition()));
            } else {
                viewers.sendPackets(clickableEntity.newDestroyPackets());
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void sendPositionChangePackets(Viewers<T> viewers) {
        if (spawnClickableEntity) {
            viewers.sendPackets(clickableEntity.newTeleportPackets(getClickableEntityPosition()));
        }
    }

    private PositionCoordinates getClickableEntityPosition() {
        return positionCoordinates.addY(positionOffsetY);
    }

}
