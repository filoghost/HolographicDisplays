/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.common.nms.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.Objects;

public class ItemLineTracker extends ClickableLineTracker<BaseItemHologramLine> {

    private final ItemNMSPacketEntity itemEntity;

    private ItemStack itemStack;
    private boolean itemStackChanged;

    private boolean spawnItemEntity;
    private boolean spawnItemEntityChanged;

    public ItemLineTracker(BaseItemHologramLine line, NMSManager nmsManager, LineClickListener lineClickListener) {
        super(line, nmsManager, lineClickListener);
        this.itemEntity = nmsManager.newItemPacketEntity();
    }

    @MustBeInvokedByOverriders
    @Override
    protected void update(Collection<? extends Player> onlinePlayers) {
        super.update(onlinePlayers);

        if (spawnItemEntity && hasTrackedPlayers() && line.hasPickupCallback()) {
            for (Player trackedPlayer : getTrackedPlayers()) {
                if (CollisionHelper.isInPickupRange(trackedPlayer, position)) {
                    line.onPickup(trackedPlayer);
                }
            }
        }
    }

    @Override
    protected boolean updatePlaceholders() {
        return false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void detectChanges() {
        super.detectChanges();

        ItemStack itemStack = line.getItemStack();
        if (!Objects.equals(this.itemStack, itemStack)) {
            this.itemStack = itemStack;
            this.itemStackChanged = true;
        }

        boolean spawnItemEntity = itemStack != null;
        if (this.spawnItemEntity != spawnItemEntity) {
            this.spawnItemEntity = spawnItemEntity;
            this.spawnItemEntityChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        super.clearDetectedChanges();
        this.itemStackChanged = false;
        this.spawnItemEntityChanged = false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addSpawnPackets(NMSPacketList packetList) {
        super.addSpawnPackets(packetList);

        if (spawnItemEntity) {
            itemEntity.addSpawnPackets(packetList, position, itemStack);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addDestroyPackets(NMSPacketList packetList) {
        super.addDestroyPackets(packetList);

        if (spawnItemEntity) {
            itemEntity.addDestroyPackets(packetList);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        super.addChangesPackets(packetList);

        if (spawnItemEntityChanged) {
            if (spawnItemEntity) {
                itemEntity.addSpawnPackets(packetList, position, itemStack);
            } else {
                itemEntity.addDestroyPackets(packetList);
            }
        } else if (itemStackChanged) {
            // Only send item changes if full spawn/destroy packets were not sent
            itemEntity.addChangePackets(packetList, itemStack);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addPositionChangePackets(NMSPacketList packetList) {
        super.addPositionChangePackets(packetList);

        if (spawnItemEntity) {
            itemEntity.addTeleportPackets(packetList, position);
        }
    }

}
