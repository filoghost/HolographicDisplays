/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_20_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;

class VersionClickableNMSPacketEntity implements ClickableNMSPacketEntity {

    private final EntityID slimeID;

    VersionClickableNMSPacketEntity(EntityID slimeID) {
        this.slimeID = slimeID;
    }

    @Override
    public EntityID getID() {
        return slimeID;
    }

    @Override
    public PacketGroup newSpawnPackets(PositionCoordinates position) {
        return PacketGroup.of(
                new EntitySpawnNMSPacket(slimeID, EntityTypeID.SLIME, position, SLIME_Y_OFFSET),
                EntityMetadataNMSPacket.builder(slimeID)
                        .setInvisible()
                        .setSlimeSmall() // Required for a correct client-side collision box
                        .build()
        );
    }

    @Override
    public PacketGroup newTeleportPackets(PositionCoordinates position) {
        return new EntityTeleportNMSPacket(slimeID, position, SLIME_Y_OFFSET);
    }

    @Override
    public PacketGroup newDestroyPackets() {
        return new EntityDestroyNMSPacket(slimeID);
    }

}
