/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.common.nms.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseClickableLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class ClickableLineTracker<T extends BaseClickableLine> extends PositionBasedLineTracker<T> {

    private final ClickableNMSPacketEntity clickableEntity;
    private final double positionOffsetY;
    private final LineClickListener lineClickListener;

    private boolean spawnClickableEntity;
    private boolean spawnClickableEntityChanged;

    public ClickableLineTracker(T line, NMSManager nmsManager, LineClickListener lineClickListener) {
        super(line);
        this.clickableEntity = nmsManager.newClickablePacketEntity();
        this.positionOffsetY = (line.getHeight() - ClickableNMSPacketEntity.SLIME_HEIGHT) / 2;
        this.lineClickListener = lineClickListener;
    }

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

        boolean spawnClickableEntity = line.getClickListener() != null;
        if (this.spawnClickableEntity != spawnClickableEntity) {
            this.spawnClickableEntity = spawnClickableEntity;
            this.spawnClickableEntityChanged = true;
            if (spawnClickableEntity) {
                lineClickListener.registerLine(clickableEntity.getID(), line);
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
    protected void addSpawnPackets(NMSPacketList packetList) {
        if (spawnClickableEntity) {
            clickableEntity.addSpawnPackets(packetList, getClickableEntityPosition());
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addDestroyPackets(NMSPacketList packetList) {
        if (spawnClickableEntity) {
            clickableEntity.addDestroyPackets(packetList);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        super.addChangesPackets(packetList);

        if (spawnClickableEntityChanged) {
            if (spawnClickableEntity) {
                clickableEntity.addSpawnPackets(packetList, getClickableEntityPosition());
            } else {
                clickableEntity.addDestroyPackets(packetList);
            }
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addPositionChangePackets(NMSPacketList packetList) {
        if (spawnClickableEntity) {
            clickableEntity.addTeleportPackets(packetList, getClickableEntityPosition());
        }
    }

    private Position getClickableEntityPosition() {
        return position.addY(positionOffsetY);
    }

}
