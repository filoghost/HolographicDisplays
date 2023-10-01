/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R2;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;

class VersionTextNMSPacketEntity implements TextNMSPacketEntity {

    private final EntityID armorStandID;

    VersionTextNMSPacketEntity(EntityID armorStandID) {
        this.armorStandID = armorStandID;
    }

    @Override
    public PacketGroup newSpawnPackets(PositionCoordinates position, String text) {
        return PacketGroup.of(
                new EntitySpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, position, ARMOR_STAND_Y_OFFSET),
                EntityMetadataNMSPacket.builder(armorStandID)
                        .setArmorStandMarker()
                        .setCustomName(text)
                        .build()
        );
    }

    @Override
    public IndividualTextPacketGroup newSpawnPackets(PositionCoordinates position) {
        return IndividualTextPacketGroup.of(
                new EntitySpawnNMSPacket(armorStandID, EntityTypeID.ARMOR_STAND, position, ARMOR_STAND_Y_OFFSET),
                (String text) -> EntityMetadataNMSPacket.builder(armorStandID)
                        .setArmorStandMarker()
                        .setCustomName(text)
                        .build()
        );
    }

    @Override
    public PacketGroup newChangePackets(String text) {
        return EntityMetadataNMSPacket.builder(armorStandID)
                .setCustomName(text)
                .build();
    }

    @Override
    public IndividualTextPacketGroup newChangePackets() {
        return IndividualTextPacketGroup.of(
                (String text) -> EntityMetadataNMSPacket.builder(armorStandID)
                        .setCustomName(text)
                        .build()
        );
    }

    @Override
    public PacketGroup newTeleportPackets(PositionCoordinates position) {
        return new EntityTeleportNMSPacket(armorStandID, position, ARMOR_STAND_Y_OFFSET);
    }

    @Override
    public PacketGroup newDestroyPackets() {
        return new EntityDestroyNMSPacket(armorStandID);
    }

}
