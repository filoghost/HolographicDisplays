/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.Position;
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
    public void addSpawnPackets(NMSPacketList packetList, Position position, String text) {
        packetList.add(new EntityLivingSpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, position, ARMOR_STAND_Y_OFFSET));
        packetList.add(EntityMetadataNMSPacket.builder(armorStandID)
                .setArmorStandMarker()
                .setCustomName(text)
                .build()
        );
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, Position position, IndividualText individualText) {
        packetList.add(new EntityLivingSpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, position, ARMOR_STAND_Y_OFFSET));
        packetList.add(new IndividualNMSPacket(player ->
                EntityMetadataNMSPacket.builder(armorStandID)
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
        packetList.add(new IndividualNMSPacket(player ->
                EntityMetadataNMSPacket.builder(armorStandID)
                        .setCustomName(individualText.get(player))
                        .build()
        ));
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityTeleportNMSPacket(armorStandID, position, ARMOR_STAND_Y_OFFSET));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        PacketHelper.addDestroyPackets(packetList, armorStandID);
    }

}
