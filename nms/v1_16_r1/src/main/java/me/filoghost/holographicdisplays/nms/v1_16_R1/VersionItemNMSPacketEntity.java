/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R1;

import me.filoghost.holographicdisplays.common.Position;
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
    public void addSpawnPackets(NMSPacketList packetList, Position position, ItemStack itemStack) {
        packetList.add(new EntityLivingSpawnNMSPacket(vehicleID, EntityTypeID.ARMOR_STAND, position, ITEM_Y_OFFSET));
        packetList.add(EntityMetadataNMSPacket.builder(vehicleID)
                .setArmorStandMarker()
                .build()
        );
        packetList.add(new EntitySpawnNMSPacket(itemID, EntityTypeID.ITEM, position, ITEM_Y_OFFSET));
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
    public void addTeleportPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityTeleportNMSPacket(vehicleID, position, ITEM_Y_OFFSET));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(itemID, vehicleID));
    }

}
