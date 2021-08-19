/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R2;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import me.filoghost.holographicdisplays.common.nms.entity.ClickableNMSPacketEntity;

public class VersionClickableNMSPacketEntity implements ClickableNMSPacketEntity {

    private final EntityID slimeID;
    private final EntityID vehicleID;

    public VersionClickableNMSPacketEntity(EntityID slimeID, EntityID vehicleID) {
        this.slimeID = slimeID;
        this.vehicleID = vehicleID;
    }

    @Override
    public EntityID getID() {
        return slimeID;
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityLivingSpawnNMSPacket(vehicleID, EntityTypeID.ARMOR_STAND, position, SLIME_Y_OFFSET));
        packetList.add(EntityMetadataNMSPacket.builder(vehicleID)
                .setArmorStandMarker()
                .build()
        );
        packetList.add(new EntityLivingSpawnNMSPacket(slimeID, EntityTypeID.SLIME, position, SLIME_Y_OFFSET));
        packetList.add(EntityMetadataNMSPacket.builder(slimeID)
                .setInvisible()
                .setSlimeSmall() // Required for a correct client-side collision box
                .build()
        );
        packetList.add(new EntityMountNMSPacket(vehicleID, slimeID));
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityTeleportNMSPacket(vehicleID, position, SLIME_Y_OFFSET));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(slimeID, vehicleID));
    }

}
