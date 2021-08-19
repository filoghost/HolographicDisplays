/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R2;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.common.nms.entity.ItemNMSPacketEntity;
import org.bukkit.inventory.ItemStack;

public class VersionItemNMSPacketEntity implements ItemNMSPacketEntity {

    private final EntityID itemID;
    private final EntityID vehicleID;

    public VersionItemNMSPacketEntity(EntityID itemID, EntityID vehicleID) {
        this.itemID = itemID;
        this.vehicleID = vehicleID;
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ, ItemStack itemStack) {
        packetList.add(new EntityLivingSpawnNMSPacket(
                vehicleID, EntityTypeID.ARMOR_STAND, positionX, positionY + ITEM_Y_OFFSET, positionZ));
        packetList.add(EntityMetadataNMSPacket.builder(vehicleID)
                .setArmorStandMarker()
                .build()
        );
        packetList.add(new EntitySpawnNMSPacket(
                itemID, EntityTypeID.ITEM, positionX, positionY + ITEM_Y_OFFSET, positionZ));
        packetList.add(EntityMetadataNMSPacket.builder(itemID)
                .setItemStack(itemStack)
                .build()
        );
        packetList.add(new EntityMountNMSPacket(vehicleID, itemID));
    }

    @Override
    public void addChangePackets(NMSPacketList packetList, ItemStack itemStack) {
        packetList.add(EntityMetadataNMSPacket.builder(itemID)
                .setItemStack(itemStack)
                .build()
        );
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ) {
        packetList.add(new EntityTeleportNMSPacket(vehicleID, positionX, positionY + ITEM_Y_OFFSET, positionZ));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(itemID, vehicleID));
    }

}
