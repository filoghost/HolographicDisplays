/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.Objects;

public class ItemLineTracker extends ClickableLineTracker<BaseItemLine> {

    private final EntityID vehicleEntityID;
    private final EntityID itemEntityID;

    private ItemStack itemStack;
    private boolean itemStackChanged;

    private boolean spawnItemEntities;
    private boolean spawnItemEntitiesChanged;

    public ItemLineTracker(BaseItemLine line, NMSManager nmsManager, LineClickListener lineClickListener) {
        super(line, nmsManager, lineClickListener);
        this.vehicleEntityID = nmsManager.newEntityID();
        this.itemEntityID = nmsManager.newEntityID();
    }

    @MustBeInvokedByOverriders
    @Override
    protected void update(Collection<? extends Player> onlinePlayers) {
        super.update(onlinePlayers);

        if (spawnItemEntities && hasTrackedPlayers()) {
            for (Player trackedPlayer : getTrackedPlayers()) {
                if (CollisionHelper.isInPickupRange(trackedPlayer, locationX, locationY, locationZ)) {
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

        boolean spawnItemEntities = itemStack != null;
        if (this.spawnItemEntities != spawnItemEntities) {
            this.spawnItemEntities = spawnItemEntities;
            this.spawnItemEntitiesChanged = true;
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void clearDetectedChanges() {
        super.clearDetectedChanges();
        this.itemStackChanged = false;
        this.spawnItemEntitiesChanged = false;
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addSpawnPackets(NMSPacketList packetList) {
        super.addSpawnPackets(packetList);

        if (spawnItemEntities) {
            addItemSpawnPackets(packetList);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addDestroyPackets(NMSPacketList packetList) {
        super.addDestroyPackets(packetList);

        if (spawnItemEntities) {
            addItemDestroyPackets(packetList);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addChangesPackets(NMSPacketList packetList) {
        super.addChangesPackets(packetList);

        if (spawnItemEntitiesChanged) {
            if (spawnItemEntities) {
                addItemSpawnPackets(packetList);
            } else {
                addItemDestroyPackets(packetList);
            }
        } else if (itemStackChanged) {
            // Only send item changes if full spawn/destroy packets we not sent
            packetList.addItemStackChangePackets(itemEntityID, itemStack);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    protected void addLocationChangePackets(NMSPacketList packetList) {
        super.addLocationChangePackets(packetList);

        if (spawnItemEntities) {
            packetList.addTeleportPackets(vehicleEntityID, locationX, getItemLocationY(), locationZ);
        }
    }

    private void addItemSpawnPackets(NMSPacketList packetList) {
        packetList.addArmorStandSpawnPackets(vehicleEntityID, locationX, getItemLocationY(), locationZ);
        packetList.addItemSpawnPackets(itemEntityID, locationX, getItemLocationY(), locationZ, itemStack);
        packetList.addMountPackets(vehicleEntityID, itemEntityID);
    }

    private void addItemDestroyPackets(NMSPacketList packetList) {
        packetList.addEntityDestroyPackets(itemEntityID, vehicleEntityID);
    }

    private double getItemLocationY() {
        return locationY;
    }

}
