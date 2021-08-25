/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R3;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;

public class VersionClickableNMSPacketEntity implements ClickableNMSPacketEntity {

    private final EntityID slimeID;

    public VersionClickableNMSPacketEntity(EntityID slimeID) {
        this.slimeID = slimeID;
    }

    @Override
    public EntityID getID() {
        return slimeID;
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityLivingSpawnNMSPacket(slimeID, EntityTypeID.SLIME, position, SLIME_Y_OFFSET));
        packetList.add(EntityMetadataNMSPacket.builder(slimeID)
                .setInvisible()
                .setSlimeSmall() // Required for a correct client-side collision box
                .build()
        );
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, Position position) {
        packetList.add(new EntityTeleportNMSPacket(slimeID, position, SLIME_Y_OFFSET));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(slimeID));
    }

}
