/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_18_R1;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;

class EntityDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityDestroyNMSPacket(EntityID entityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarIntArray(entityID.getNumericID());

        this.rawPacket = new PacketPlayOutEntityDestroy(packetByteBuffer.getInternalSerializer());
    }

    EntityDestroyNMSPacket(EntityID entityID1, EntityID entityID2) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarIntArray(entityID1.getNumericID(), entityID2.getNumericID());

        this.rawPacket = new PacketPlayOutEntityDestroy(packetByteBuffer.getInternalSerializer());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
