/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_15_R1;

import me.filoghost.holographicdisplays.common.nms.AbstractNMSPacketList;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.IndividualCustomName;
import me.filoghost.holographicdisplays.common.nms.IndividualNMSPacket;
import org.bukkit.inventory.ItemStack;

class VersionNMSPacketList extends AbstractNMSPacketList {

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        add(EntityMetadataNMSPacket.builder(entityID)
                .setArmorStandMarker()
                .build()
        );
    }

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, String customName) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        add(EntityMetadataNMSPacket.builder(entityID)
                .setArmorStandMarker()
                .setCustomName(customName)
                .build()
        );
    }

    @Override
    public void addArmorStandSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, IndividualCustomName individualCustomName) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        add(new IndividualNMSPacket(player -> EntityMetadataNMSPacket.builder(entityID)
                .setArmorStandMarker()
                .setCustomName(individualCustomName.get(player))
                .build()
        ));
    }

    @Override
    public void addArmorStandNameChangePackets(EntityID entityID, String customName) {
        add(EntityMetadataNMSPacket.builder(entityID)
                .setCustomName(customName)
                .build()
        );
    }

    @Override
    public void addArmorStandNameChangePackets(EntityID entityID, IndividualCustomName individualCustomName) {
        add(new IndividualNMSPacket(player -> EntityMetadataNMSPacket.builder(entityID)
                .setCustomName(individualCustomName.get(player))
                .build()
        ));
    }

    @Override
    public void addItemSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ, ItemStack itemStack) {
        add(new EntitySpawnNMSPacket(entityID, EntityTypeID.ITEM, positionX, positionY, positionZ));
        add(EntityMetadataNMSPacket.builder(entityID)
                .setItemStack(itemStack)
                .build()
        );
    }

    @Override
    public void addItemStackChangePackets(EntityID entityID, ItemStack itemStack) {
        add(EntityMetadataNMSPacket.builder(entityID)
                .setItemStack(itemStack)
                .build()
        );
    }

    @Override
    public void addSlimeSpawnPackets(EntityID entityID, double positionX, double positionY, double positionZ) {
        add(new EntityLivingSpawnNMSPacket(entityID, EntityTypeID.SLIME, positionX, positionY, positionZ));
        add(EntityMetadataNMSPacket.builder(entityID)
                .setInvisible()
                .setSlimeSmall() // Required for a correct client-side collision box
                .build()
        );
    }

    @Override
    public void addEntityDestroyPackets(EntityID... entityIDs) {
        add(new EntityDestroyNMSPacket(entityIDs));
    }

    @Override
    public void addTeleportPackets(EntityID entityID, double positionX, double positionY, double positionZ) {
        add(new EntityTeleportNMSPacket(entityID, positionX, positionY, positionZ));
    }

    @Override
    public void addMountPackets(EntityID vehicleEntityID, EntityID passengerEntityID) {
        add(new EntityMountNMSPacket(vehicleEntityID, passengerEntityID));
    }

}
