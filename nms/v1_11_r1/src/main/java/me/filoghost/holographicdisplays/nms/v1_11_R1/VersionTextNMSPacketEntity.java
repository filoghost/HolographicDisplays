/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_11_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.IndividualNMSPacket;
import me.filoghost.holographicdisplays.common.nms.IndividualText;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.common.nms.entity.TextNMSPacketEntity;

class VersionTextNMSPacketEntity implements TextNMSPacketEntity {

    private final EntityID armorStandID;

    VersionTextNMSPacketEntity(EntityID armorStandID) {
        this.armorStandID = armorStandID;
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ) {
        packetList.add(new EntitySpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        packetList.add(EntityMetadataNMSPacket.builder(armorStandID)
                .setArmorStandMarker()
                .build()
        );
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ, String text) {
        packetList.add(new EntitySpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        packetList.add(EntityMetadataNMSPacket.builder(armorStandID)
                .setArmorStandMarker()
                .setCustomName(text)
                .build()
        );
    }

    @Override
    public void addSpawnPackets(
            NMSPacketList packetList, double positionX, double positionY, double positionZ, IndividualText individualText) {
        packetList.add(new EntitySpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, positionX, positionY, positionZ));
        packetList.add(new IndividualNMSPacket(player -> EntityMetadataNMSPacket.builder(armorStandID)
                .setArmorStandMarker()
                .setCustomName(individualText.get(player))
                .build()
        ));
    }

    @Override
    public void addChangePackets(NMSPacketList packetList, String text) {
        packetList.add(EntityMetadataNMSPacket.builder(armorStandID)
                .setCustomName(text)
                .build()
        );
    }

    @Override
    public void addChangePackets(NMSPacketList packetList, IndividualText individualText) {
        packetList.add(new IndividualNMSPacket(player -> EntityMetadataNMSPacket.builder(armorStandID)
                .setCustomName(individualText.get(player))
                .build()
        ));
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ) {
        packetList.add(new EntityTeleportNMSPacket(armorStandID, positionX, positionY, positionZ));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(armorStandID));
    }

}
