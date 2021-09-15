/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseClickableHologramLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class ClickableLineTracker<T extends Viewer> extends PositionBasedLineTracker<T> {

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

    @Override
    protected abstract BaseClickableHologramLine getLine();

    @MustBeInvokedByOverriders
    @Override
    public void onRemoval() {
        super.onRemoval();
        lineClickListener.unregisterLine(clickableEntity.getID());
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
                lineClickListener.registerLine(clickableEntity.getID(), getLine());
            } else {
                lineClickListener.unregisterLine(clickableEntity.getID());
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
        return position.addY(positionOffsetY);
    }

}
